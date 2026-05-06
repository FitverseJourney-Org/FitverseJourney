package com.example.presentation.ui.workout

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.TimerManager
import com.example.expect.PlatformDateFormatter
import com.example.expect.formatWorkoutTime
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

// ============================================================================
// DATA MODELS - Análise de Treino
// ============================================================================

/**
 * Resultado completo do treino com análise de performance
 */
data class WorkoutCompletionResult(
    val workoutPlan: WorkoutPlan,
    val completedExercises: List<CompletedExerciseAnalysis>,
    val totalDurationSeconds: Int,
    val totalXp: Int,
    val bonusXp: Int = 0,
    val totalVolume: Float = 0f, // kg total movimentado
    val totalReps: Int = 0,
    val totalSets: Int = 0,
    val personalRecords: List<PersonalRecord> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val improvementPercentage: Float = 0f,
    val completedAt: Long = TimerManager.nowMillis()
)

/**
 * Análise individual de cada exercício
 */
data class CompletedExerciseAnalysis(
    val exercise: Exercise,
    val completedSets: List<SetRecord>,
    val volume: Float = 0f,
    val averageWeight: Float = 0f,
    val totalReps: Int = 0,
    val isPR: Boolean = false,
    val improvementFromLast: Float = 0f,
    val previousBest: Float = 0f
)

/**
 * Record pessoal batido
 */
data class PersonalRecord(
    val exerciseId: Int,
    val exerciseName: String,
    val type: PRType,
    val value: Float,
    val previousValue: Float,
    val improvement: Float,
    val bonusXp: Int = 50
)

enum class PRType {
    MAX_WEIGHT,      // Maior peso usado
    MAX_VOLUME,      // Maior volume total
    MAX_REPS,        // Mais repetições
    FASTEST_TIME     // Menor tempo (para exercícios de tempo)
}

/**
 * Conquista/Achievement desbloqueado
 */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String, // Emoji ou icon name
    val xpBonus: Int = 25,
    val rarity: AchievementRarity = AchievementRarity.COMMON
)

enum class AchievementRarity {
    COMMON, RARE, EPIC, LEGENDARY
}

/**
 * Estatística comparativa
 */
data class WorkoutComparison(
    val averageDuration: Int,
    val averageVolume: Float,
    val averageXp: Int,
    val improvementTrend: Float // Positivo = melhorando, Negativo = piorando
)

// ============================================================================
// WORKOUT COMPLETION ANALYZER - Lógica de análise
// ============================================================================

object WorkoutAnalyzer {

    /**
     * Analisa o treino completo e retorna resultado com PRs e achievements
     */
    fun analyzeWorkout(
        workoutPlan: WorkoutPlan,
        allExerciseSets: Map<Int, List<SetRecord>>,
        durationSeconds: Int,
        totalXp: Int,
        exerciseHistory: Map<Int, List<PastWorkoutLog>>
    ): WorkoutCompletionResult {

        val completedExercises = mutableListOf<CompletedExerciseAnalysis>()
        val personalRecords = mutableListOf<PersonalRecord>()
        var totalVolume = 0f
        var totalReps = 0
        var totalSets = 0

        // Analisa cada exercício
        workoutPlan.exercises.forEach { exercise ->
            val sets = allExerciseSets[exercise.id]?.filter { it.isCompleted } ?: emptyList()
            if (sets.isEmpty()) return@forEach

            val analysis = analyzeExercise(exercise, sets, exerciseHistory[exercise.id] ?: emptyList())
            completedExercises.add(analysis)

            totalVolume += analysis.volume
            totalReps += analysis.totalReps
            totalSets += sets.size

            // Verifica PRs
            if (analysis.isPR) {
                val pr = detectPersonalRecord(exercise, sets, exerciseHistory[exercise.id] ?: emptyList())
                pr?.let { personalRecords.add(it) }
            }
        }

        // Calcula bônus de XP
        val bonusXp = calculateBonusXp(personalRecords, completedExercises, durationSeconds)

        // Detecta achievements
        val achievements = detectAchievements(completedExercises, personalRecords, totalSets, durationSeconds)

        // Calcula melhoria geral
        val improvementPercentage = calculateOverallImprovement(completedExercises)

        return WorkoutCompletionResult(
            workoutPlan = workoutPlan,
            completedExercises = completedExercises,
            totalDurationSeconds = durationSeconds,
            totalXp = totalXp,
            bonusXp = bonusXp,
            totalVolume = totalVolume,
            totalReps = totalReps,
            totalSets = totalSets,
            personalRecords = personalRecords,
            achievements = achievements,
            improvementPercentage = improvementPercentage
        )
    }

    private fun analyzeExercise(
        exercise: Exercise,
        completedSets: List<SetRecord>,
        history: List<PastWorkoutLog>
    ): CompletedExerciseAnalysis {

        val volume = if (exercise.type == ExerciseType.REPS) {
            completedSets.sumOf {
                val weight = it.weight.toFloatOrNull() ?: 0f
                val reps = it.reps.toIntOrNull() ?: 0
                (weight * reps).toDouble()
            }.toFloat()
        } else 0f

        val totalReps = completedSets.sumOf { it.reps.toIntOrNull() ?: 0 }

        val averageWeight = if (exercise.type == ExerciseType.REPS && completedSets.isNotEmpty()) {
            completedSets.mapNotNull { it.weight.toFloatOrNull() }.average().toFloat()
        } else 0f

        // Compara com histórico
        val previousBest = history.maxOfOrNull { it.totalVolume } ?: 0f
        val isPR = volume > previousBest && previousBest > 0
        val improvement = if (previousBest > 0) {
            ((volume - previousBest) / previousBest) * 100
        } else 0f

        return CompletedExerciseAnalysis(
            exercise = exercise,
            completedSets = completedSets,
            volume = volume,
            averageWeight = averageWeight,
            totalReps = totalReps,
            isPR = isPR,
            improvementFromLast = improvement,
            previousBest = previousBest
        )
    }

    private fun detectPersonalRecord(
        exercise: Exercise,
        sets: List<SetRecord>,
        history: List<PastWorkoutLog>
    ): PersonalRecord? {

        if (exercise.type != ExerciseType.REPS || history.isEmpty()) return null

        val currentMaxWeight = sets.mapNotNull { it.weight.toFloatOrNull() }.maxOrNull() ?: 0f
        val currentVolume = sets.sumOf {
            val w = it.weight.toFloatOrNull() ?: 0f
            val r = it.reps.toIntOrNull() ?: 0
            (w * r).toDouble()
        }.toFloat()

        val previousMaxWeight = history.flatMap { it.sets }
            .mapNotNull { it.weight.toFloatOrNull() }
            .maxOrNull() ?: 0f

        val previousMaxVolume = history.maxOfOrNull { it.totalVolume } ?: 0f

        // Verifica PR de peso máximo
        if (currentMaxWeight > previousMaxWeight && previousMaxWeight > 0) {
            return PersonalRecord(
                exerciseId = exercise.id,
                exerciseName = exercise.title,
                type = PRType.MAX_WEIGHT,
                value = currentMaxWeight,
                previousValue = previousMaxWeight,
                improvement = currentMaxWeight - previousMaxWeight,
                bonusXp = 50
            )
        }

        // Verifica PR de volume
        if (currentVolume > previousMaxVolume && previousMaxVolume > 0) {
            return PersonalRecord(
                exerciseId = exercise.id,
                exerciseName = exercise.title,
                type = PRType.MAX_VOLUME,
                value = currentVolume,
                previousValue = previousMaxVolume,
                improvement = ((currentVolume - previousMaxVolume) / previousMaxVolume) * 100,
                bonusXp = 75
            )
        }

        return null
    }

    private fun calculateBonusXp(
        personalRecords: List<PersonalRecord>,
        exercises: List<CompletedExerciseAnalysis>,
        durationSeconds: Int
    ): Int {
        var bonus = 0

        // Bônus por PRs
        bonus += personalRecords.sumOf { it.bonusXp }

        // Bônus por completar tudo
        bonus += 50

        // Bônus por velocidade (se completou em menos de 45 min)
        if (durationSeconds < 45 * 60) {
            bonus += 25
        }

        // Bônus por consistência (todos exercícios com melhoria)
        if (exercises.all { it.improvementFromLast > 0 || it.previousBest == 0f }) {
            bonus += 100
        }

        return bonus
    }

    private fun detectAchievements(
        exercises: List<CompletedExerciseAnalysis>,
        prs: List<PersonalRecord>,
        totalSets: Int,
        durationSeconds: Int
    ): List<Achievement> {

        val achievements = mutableListOf<Achievement>()

        // First PR ever
        if (prs.isNotEmpty() && exercises.any { it.previousBest == 0f }) {
            achievements.add(
                Achievement(
                    id = "first_pr",
                    title = "First Blood! 🎯",
                    description = "Bateu seu primeiro Personal Record!",
                    icon = "🎯",
                    xpBonus = 50,
                    rarity = AchievementRarity.RARE
                )
            )
        }

        // PR Streak (múltiplos PRs no mesmo treino)
        if (prs.size >= 3) {
            achievements.add(
                Achievement(
                    id = "pr_streak",
                    title = "On Fire! 🔥",
                    description = "Bateu ${prs.size} PRs em um único treino!",
                    icon = "🔥",
                    xpBonus = 100,
                    rarity = AchievementRarity.EPIC
                )
            )
        }

        // Volume Monster (muito volume total)
        val totalVolume = exercises.sumOf { it.volume.toDouble() }.toFloat()
        if (totalVolume > 5000) {
            achievements.add(
                Achievement(
                    id = "volume_monster",
                    title = "Volume Monster 💪",
                    description = "Movimentou mais de 5 toneladas!",
                    icon = "💪",
                    xpBonus = 75,
                    rarity = AchievementRarity.RARE
                )
            )
        }

        // Speed Demon (treino rápido)
        if (durationSeconds < 30 * 60 && totalSets >= 12) {
            achievements.add(
                Achievement(
                    id = "speed_demon",
                    title = "Speed Demon ⚡",
                    description = "Completou treino intenso em tempo recorde!",
                    icon = "⚡",
                    xpBonus = 50,
                    rarity = AchievementRarity.RARE
                )
            )
        }

        // Perfect Form (todas as séries completas)
        if (exercises.all { it.completedSets.size >= it.exercise.sets }) {
            achievements.add(
                Achievement(
                    id = "perfect_form",
                    title = "Perfect Form ✨",
                    description = "Completou todas as séries planejadas!",
                    icon = "✨",
                    xpBonus = 25,
                    rarity = AchievementRarity.COMMON
                )
            )
        }

        // Improvement Streak (melhorou em todos exercícios)
        if (exercises.size >= 3 && exercises.all { it.improvementFromLast > 0 || it.previousBest == 0f }) {
            achievements.add(
                Achievement(
                    id = "improvement_streak",
                    title = "Improvement Streak 📈",
                    description = "Melhorou em todos os exercícios!",
                    icon = "📈",
                    xpBonus = 75,
                    rarity = AchievementRarity.EPIC
                )
            )
        }

        return achievements
    }

    private fun calculateOverallImprovement(
        exercises: List<CompletedExerciseAnalysis>
    ): Float {
        val improvementsWithHistory = exercises.filter { it.previousBest > 0 }
        return if (improvementsWithHistory.isNotEmpty()) {
            improvementsWithHistory.map { it.improvementFromLast }.average().toFloat()
        } else 0f
    }
}

// ============================================================================
// WORKOUT COMPLETION SCREEN - Tela de Conclusão
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCompletionScreen(
    result: WorkoutCompletionResult,
    onContinue: () -> Unit,
    onShareWorkout: () -> Unit = {},
    onViewStats: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    // Animações de entrada
    var showContent by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
        if (result.personalRecords.isNotEmpty() || result.achievements.isNotEmpty()) {
            delay(500)
            showConfetti = true
        }
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0,0,0,0),
                title = { Text("Workout Complete! 🎉") },
                actions = {
                    IconButton(onClick = onShareWorkout) {
                        Icon(Icons.Rounded.Share, "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                onContinue = onContinue,
                onViewStats = onViewStats
            )
        }
    ){ padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Celebration Header
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn() + expandVertically()
                    ) {
                        CelebrationHeader(
                            result = result,
                            showConfetti = showConfetti
                        )
                    }
                }

                // XP Summary
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = 200))
                    ) {
                        XpSummaryCard(
                            baseXp = result.totalXp,
                            bonusXp = result.bonusXp,
                            totalXp = result.totalXp + result.bonusXp
                        )
                    }
                }

                // Personal Records
                if (result.personalRecords.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = showContent,
                            enter = fadeIn(animationSpec = tween(600, delayMillis = 400))
                        ) {
                            PersonalRecordsSection(personalRecords = result.personalRecords)
                        }
                    }
                }

                // Achievements
                if (result.achievements.isNotEmpty()) {
                    item {
                        AnimatedVisibility(
                            visible = showContent,
                            enter = fadeIn(animationSpec = tween(600, delayMillis = 600))
                        ) {
                            AchievementsSection(achievements = result.achievements)
                        }
                    }
                }

                // Stats Overview
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = 800))
                    ) {
                        StatsOverviewSection(result = result)
                    }
                }

                // Exercise Breakdown
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = 1000))
                    ) {
                        ExerciseBreakdownSection(
                            exercises = result.completedExercises
                        )
                    }
                }

                // Motivational Quote / Next Steps
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(600, delayMillis = 1200))
                    ) {
                        NextStepsCard(
                            improvementPercentage = result.improvementPercentage,
                            hasPRs = result.personalRecords.isNotEmpty()
                        )
                    }
                }
            }

            // Confetti animation overlay
            if (showConfetti) {
                ConfettiEffect()
            }
        }
    }
}

// ============================================================================
// CELEBRATION HEADER
// ============================================================================

@Composable
fun CelebrationHeader(
    result: WorkoutCompletionResult,
    showConfetti: Boolean
) {
    val cs = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.primaryContainer
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Trophy icon animado
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(if (showConfetti) scale else 1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        result.personalRecords.size >= 3 -> "🏆"
                        result.personalRecords.isNotEmpty() -> "🎯"
                        result.achievements.size >= 3 -> "⭐"
                        else -> "💪"
                    },
                    fontSize = 64.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = when {
                    result.personalRecords.size >= 3 -> "Legendary Performance!"
                    result.personalRecords.isNotEmpty() -> "New Personal Record!"
                    result.improvementPercentage > 10 -> "Crushing It!"
                    else -> "Workout Complete!"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = cs.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = result.workoutPlan.title,
                style = MaterialTheme.typography.titleMedium,
                color = cs.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = PlatformDateFormatter.formatFullDate(result.completedAt),
                style = MaterialTheme.typography.bodySmall,
                color = cs.onPrimaryContainer.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// XP SUMMARY CARD
// ============================================================================

@Composable
fun XpSummaryCard(
    baseXp: Int,
    bonusXp: Int,
    totalXp: Int
) {
    val cs = MaterialTheme.colorScheme
    var animatedXp by remember { mutableIntStateOf(0) }

    LaunchedEffect(totalXp) {
        var current = 0
        while (current < totalXp) {
            current = (current + totalXp / 50).coerceAtMost(totalXp)
            animatedXp = current
            delay(20)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.tertiaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Experience Earned",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onTertiaryContainer
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = animatedXp.toString(),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = cs.onTertiaryContainer
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "XP",
                            style = MaterialTheme.typography.titleLarge,
                            color = cs.onTertiaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                Surface(
                    color = cs.tertiary,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("⚡", fontSize = 32.sp)
                    }
                }
            }

            if (bonusXp > 0) {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = cs.onTertiaryContainer.copy(alpha = 0.2f))
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Base XP",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        "+$baseXp",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.onTertiaryContainer
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Celebration,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = cs.tertiary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Bonus XP",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = cs.tertiary
                        )
                    }
                    Text(
                        "+$bonusXp",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.tertiary
                    )
                }
            }
        }
    }
}

// ============================================================================
// PERSONAL RECORDS SECTION
// ============================================================================

@Composable
fun PersonalRecordsSection(
    personalRecords: List<PersonalRecord>
) {
    val cs = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Personal Records",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                color = cs.primaryContainer,
                shape = CircleShape
            ) {
                Text(
                    text = personalRecords.size.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = cs.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        personalRecords.forEach { pr ->
            PersonalRecordCard(pr = pr)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun PersonalRecordCard(pr: PersonalRecord) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.errorContainer.copy(alpha = 0.3f)
        ),
        border = BorderStroke(2.dp, cs.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = cs.primary,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🏆", fontSize = 24.sp)
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        pr.exerciseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        when (pr.type) {
                            PRType.MAX_WEIGHT -> "New Max Weight"
                            PRType.MAX_VOLUME -> "New Max Volume"
                            PRType.MAX_REPS -> "New Max Reps"
                            PRType.FASTEST_TIME -> "Fastest Time"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = when (pr.type) {
                        PRType.MAX_WEIGHT -> "${pr.value.roundToInt()} kg"
                        PRType.MAX_VOLUME -> "${pr.value.roundToInt()} kg"
                        PRType.MAX_REPS -> "${pr.value.roundToInt()} reps"
                        PRType.FASTEST_TIME -> formatWorkoutTime(pr.value.toInt())
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = cs.primary
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = cs.tertiary
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "+${pr.improvement.roundToInt()}${if (pr.type == PRType.MAX_VOLUME) "%" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.tertiary
                    )
                }
            }
        }
    }
}

// ============================================================================
// ACHIEVEMENTS SECTION
// ============================================================================

@Composable
fun AchievementsSection(
    achievements: List<Achievement>
) {
    val cs = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Workspaces,
                contentDescription = null,
                tint = cs.secondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Achievements Unlocked",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievements) { achievement ->
                AchievementBadge(achievement = achievement)
            }
        }
    }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
    val cs = MaterialTheme.colorScheme

    val badgeColor = when (achievement.rarity) {
        AchievementRarity.COMMON -> cs.surfaceVariant
        AchievementRarity.RARE -> cs.secondaryContainer
        AchievementRarity.EPIC -> cs.tertiaryContainer
        AchievementRarity.LEGENDARY -> cs.primaryContainer
    }

    val borderColor = when (achievement.rarity) {
        AchievementRarity.COMMON -> Color.Transparent
        AchievementRarity.RARE -> cs.secondary
        AchievementRarity.EPIC -> cs.tertiary
        AchievementRarity.LEGENDARY -> cs.primary
    }

    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = badgeColor),
        border = if (borderColor != Color.Transparent) {
            BorderStroke(2.dp, borderColor)
        } else null,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = achievement.icon,
                fontSize = 48.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                achievement.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                achievement.description,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 3
            )

            Spacer(Modifier.height(8.dp))

            Surface(
                color = cs.primary.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    "+${achievement.xpBonus} XP",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.primary
                )
            }
        }
    }
}

// ============================================================================
// STATS OVERVIEW
// ============================================================================

@Composable
fun StatsOverviewSection(
    result: WorkoutCompletionResult
) {
    val cs = MaterialTheme.colorScheme

    Column {
        Text(
            "Workout Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatBox(
                icon = Icons.Rounded.Timer,
                label = "Duration",
                value = formatWorkoutTime(result.totalDurationSeconds),
                modifier = Modifier.weight(1f)
            )

            StatBox(
                icon = Icons.Rounded.FitnessCenter,
                label = "Volume",
                value = "${(result.totalVolume / 1000).roundToInt()}t",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatBox(
                icon = Icons.Rounded.Refresh,
                label = "Total Sets",
                value = result.totalSets.toString(),
                modifier = Modifier.weight(1f)
            )

            StatBox(
                icon = Icons.Rounded.Numbers,
                label = "Total Reps",
                value = result.totalReps.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        if (result.improvementPercentage > 0) {
            Spacer(Modifier.height(12.dp))

            Surface(
                color = cs.tertiaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.TrendingUp,
                            contentDescription = null,
                            tint = cs.tertiary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Overall Improvement",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        "+${result.improvementPercentage.roundToInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun StatBox(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        color = cs.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = cs.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant
            )
        }
    }
}

// ============================================================================
// EXERCISE BREAKDOWN
// ============================================================================

@Composable
fun ExerciseBreakdownSection(
    exercises: List<CompletedExerciseAnalysis>
) {
    val cs = MaterialTheme.colorScheme

    Column {
        Text(
            "Exercise Breakdown",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        exercises.forEach { exercise ->
            ExerciseBreakdownCard(exercise = exercise)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ExerciseBreakdownCard(
    exercise: CompletedExerciseAnalysis
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        exercise.exercise.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${exercise.completedSets.size} sets completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                }

                if (exercise.isPR) {
                    Surface(
                        color = cs.primaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏆", fontSize = 12.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "PR!",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = cs.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            if (exercise.exercise.type == ExerciseType.REPS) {
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MiniStat(
                        label = "Volume",
                        value = "${exercise.volume.roundToInt()} kg"
                    )

                    VerticalDivider(
                        modifier = Modifier.height(40.dp),
                        color = cs.outlineVariant
                    )

                    MiniStat(
                        label = "Avg Weight",
                        value = "${exercise.averageWeight.roundToInt()} kg"
                    )

                    VerticalDivider(
                        modifier = Modifier.height(40.dp),
                        color = cs.outlineVariant
                    )

                    MiniStat(
                        label = "Total Reps",
                        value = exercise.totalReps.toString()
                    )
                }

                if (exercise.improvementFromLast > 0) {
                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = cs.tertiaryContainer.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.TrendingUp,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = cs.tertiary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "+${exercise.improvementFromLast.roundToInt()}% from last workout",
                                style = MaterialTheme.typography.bodySmall,
                                color = cs.tertiary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniStat(
    label: String,
    value: String
) {
    val cs = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = cs.primary
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = cs.onSurfaceVariant
        )
    }
}

// ============================================================================
// NEXT STEPS CARD
// ============================================================================

@Composable
fun NextStepsCard(
    improvementPercentage: Float,
    hasPRs: Boolean
) {
    val cs = MaterialTheme.colorScheme

    val motivationalQuote = when {
        hasPRs -> "You're getting stronger every day! 💪"
        improvementPercentage > 10 -> "Incredible progress! Keep crushing it! 🔥"
        improvementPercentage > 0 -> "Consistency is key. You're on the right track! ⭐"
        else -> "Great start! Tomorrow you'll be even better! 🚀"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.secondaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                motivationalQuote,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = cs.onSecondaryContainer
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Rest and recover. Your next workout is waiting!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = cs.onSecondaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

// ============================================================================
// CONFETTI EFFECT
// ============================================================================

@Composable
fun ConfettiEffect() {
    // Simplified confetti - em produção você usaria uma lib como Konfetti
    val confettiColors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFF38181)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(20) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "confetti_$index")

            val offsetY by infiniteTransition.animateFloat(
                initialValue = -100f,
                targetValue = 2000f,
                animationSpec = infiniteRepeatable(
                    animation = tween((2000..3000).random(), easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "y_$index"
            )

            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween((1000..2000).random(), easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation_$index"
            )

            Box(
                modifier = Modifier
                    .offset(
                        x = (index * 50).dp,
                        y = offsetY.dp
                    )
                    .size(8.dp)
                    .rotate(rotation)
                    .background(
                        confettiColors[index % confettiColors.size],
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

// ============================================================================
// BOTTOM ACTION BAR
// ============================================================================

@Composable
fun BottomActionBar(
    onContinue: () -> Unit,
    onViewStats: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        color = cs.background,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary
                )
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onViewStats,
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.5.dp, cs.primary)
            ) {
                Icon(
                    Icons.Rounded.Analytics,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "View Detailed Stats",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}