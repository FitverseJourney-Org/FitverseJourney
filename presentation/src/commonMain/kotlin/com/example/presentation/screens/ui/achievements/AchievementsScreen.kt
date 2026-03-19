package com.example.presentation.screens.ui.achievements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. Modelagem ---
enum class AchievementSaga(val title: String, val color: Color, val icon: ImageVector) {
    STEPS("O Caminhante", Color(0xFF4CAF50), Icons.Default.DirectionsWalk),
    STRENGTH("Força Bruta", Color(0xFFF44336), Icons.Default.FitnessCenter),
    CONSISTENCY("Disciplina de Aço", Color(0xFF2196F3), Icons.Default.CalendarMonth),
    LIFESTYLE("Estilo de Vida", Color(0xFFFF9800), Icons.Default.LocalFireDepartment)
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val saga: AchievementSaga,
    val isUnlocked: Boolean,
    val progress: Float = 0f,
    val progressLabel: String? = null
)

// --- 2. Tela Principal ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    navigateBack: () -> Unit
) {
    val achievements = remember { getFullAchievementList() }
    val unlockedCount = achievements.count { it.isUnlocked }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Galeria de Troféus", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                windowInsets = WindowInsets(0,0,0,0)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header de Resumo Profissional
            item {
                AchievementSummaryCard(
                    unlockedCount = unlockedCount,
                    totalCount = achievements.size
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Listagem por Sagas
            AchievementSaga.entries.forEach { saga ->
                val sagaAchievements = achievements.filter { it.saga == saga }

                item {
                    SagaHeader(saga)
                }

                items(sagaAchievements) { achievement ->
                    AchievementItem(achievement)
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
fun AchievementSummaryCard(unlockedCount: Int, totalCount: Int) {
    val progress = if (totalCount > 0) unlockedCount.toFloat() / totalCount else 0f
    val percentage = (progress * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Progresso Geral", style = MaterialTheme.typography.labelLarge)
                Text(
                    "$unlockedCount / $totalCount Conquistas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(0.1f)
                )
            }
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SagaHeader(saga: AchievementSaga) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    ) {
        Icon(saga.icon, null, tint = saga.color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = saga.title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            color = saga.color,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    val isUnlocked = achievement.isUnlocked

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Aplica o border personalizado apenas se estiver desbloqueado
            .then(
                if (isUnlocked) Modifier.sagaBorder(achievement.saga.color.copy(alpha = 0.5f))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnlocked) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) achievement.saga.color.copy(0.1f) else Color.LightGray.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Rounded.EmojiEvents else Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) achievement.saga.color else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) Color.Unspecified else Color.Gray
                )
                Text(
                    achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isUnlocked && achievement.progress > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { achievement.progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = achievement.saga.color.copy(0.6f)
                    )
                }
            }
        }
    }
}

private fun Modifier.sagaBorder(color: Color): Modifier =
    this.border(1.dp, color, RoundedCornerShape(16.dp))


// --- 4. DATA SEED (50 Desafios) ---
fun getFullAchievementList(): List<Achievement> {
    val list = mutableListOf<Achievement>()

    // SAGAS: STEPS (12)
    list.add(Achievement("s1", "Primeiros Passos", "Alcance 5.000 passos em um dia.", AchievementSaga.STEPS, true))
    list.add(Achievement("s2", "Meta Batida", "Alcance 10.000 passos em um dia.", AchievementSaga.STEPS, true))
    list.add(Achievement("s3", "Maratonista Semanal", "70.000 passos em uma semana.", AchievementSaga.STEPS, false, 0.4f, "28k/70k"))
    list.add(Achievement("s4", "Inabalável", "Meta de passos por 7 dias seguidos.", AchievementSaga.STEPS, false))
    list.add(Achievement("s5", "Pés de Mercúrio", "Alcance 15.000 passos em um dia.", AchievementSaga.STEPS, false))
    // ... adicione as variações de passos até completar 12

    // SAGAS: STRENGTH (15)
    list.add(Achievement("f1", "Peso Pena", "Registre sua primeira progressão.", AchievementSaga.STRENGTH, true))
    list.add(Achievement("f2", "Clube dos 100", "Levante 100kg em uma única série.", AchievementSaga.STRENGTH, true))
    list.add(Achievement("f3", "Supino de Respeito", "Levante seu peso corporal no supino.", AchievementSaga.STRENGTH, false, 0.7f, "60/85kg"))
    list.add(Achievement("f4", "Titã do Agachamento", "Agache com 1.5x seu peso.", AchievementSaga.STRENGTH, false))
    list.add(Achievement("f5", "Volume Absurdo", "Movimente 5 toneladas em um treino.", AchievementSaga.STRENGTH, false))
    // ... adicione recordes para Terra, Leg Press, etc até 15

    // SAGAS: CONSISTENCY (13)
    list.add(Achievement("c1", "Semana Santa", "Treine 5 dias em uma semana.", AchievementSaga.CONSISTENCY, true))
    list.add(Achievement("c2", "Habitual", "Complete 10 treinos no total.", AchievementSaga.CONSISTENCY, true))
    list.add(Achievement("c3", "Veterano", "Complete 50 treinos no total.", AchievementSaga.CONSISTENCY, false, 0.6f, "30/50"))
    list.add(Achievement("c4", "Mês Perfeito", "30 dias sem faltar nenhum treino.", AchievementSaga.CONSISTENCY, false))
    list.add(Achievement("c5", "Lobo Solitário", "Treine em uma noite de sexta-feira.", AchievementSaga.CONSISTENCY, true))
    // ... adicione metas de meses, anos e frequências até 13

    // SAGAS: LIFESTYLE (10)
    list.add(Achievement("l1", "Madrugador", "Treine antes das 07:00 da manhã.", AchievementSaga.LIFESTYLE, true))
    list.add(Achievement("l2", "Hidratado", "Beba 2L de água por dia por 7 dias.", AchievementSaga.LIFESTYLE, false, 0.3f, "2/7"))
    list.add(Achievement("l3", "Sem Desculpas", "Registre um treino em um feriado.", AchievementSaga.LIFESTYLE, false))
    list.add(Achievement("l4", "Explorador", "Tente um exercício novo.", AchievementSaga.LIFESTYLE, true))
    list.add(Achievement("l5", "Focado", "Sem celular entre as séries por 1 treino.", AchievementSaga.LIFESTYLE, false))
    // ... complete até 10

    return list
}