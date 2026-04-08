package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar

// --- MODELOS ---

enum class DayOfWeek(val shortName: String, val fullName: String) {
    MON("S", "Segunda"), TUE("T", "Terça"), WED("Q", "Quarta"),
    THU("Q", "Quinta"), FRI("S", "Sexta"), SAT("S", "Sábado"), SUN("D", "Domingo")
}

data class Exercise(
    val id: String = "1",
    val name: String,
    val sets: String = "3",
    val reps: String = "12",
    val rest: String = "60s"
)

// --- TELA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanBuilderScreen(
    onBack: () -> Unit,
    toAddExercises: () -> Unit,
    savedPlan: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var planName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(DayOfWeek.MON) }

    val restDaysMap = remember { mutableStateMapOf<DayOfWeek, Boolean>() }
    val workoutMap = remember { mutableStateMapOf<DayOfWeek, List<Exercise>>() }
    val isCurrentDayRest = restDaysMap[selectedDay] ?: false

    // Gradiente dinâmico: Surface (#16171D) para Background (#0A0B0F)
    val screenGradient = Brush.verticalGradient(
        colors = listOf(colors.surface, colors.background)
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopAppBar(
                title = "MONTAR MISSÃO",
                onBack = onBack,
                actions = {
                    TextButton(onClick = {
                        savedPlan()
                    }) {
                        // Action principal em Secondary (Azul)
                        Text("SALVAR", color = colors.secondary, fontWeight = FontWeight.Black)
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isCurrentDayRest) {
                ExtendedFloatingActionButton(
                    onClick = toAddExercises,
                    containerColor = colors.primary, // Roxo Vibrante para ação principal
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Rounded.Add, null) },
                    text = { Text("ADICIONAR EXERCÍCIO", fontWeight = FontWeight.Black) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PlanHeaderSection(planName, colors) { planName = it }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "CRONOGRAMA SEMANAL",
                style = MaterialTheme.typography.labelLarge,
                color = colors.secondary, // Azul para subtítulos de seção
                modifier = Modifier.padding(horizontal = 20.dp),
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            DaySelector(selectedDay, colors) { selectedDay = it }

            ExerciseListSection(
                dayName = selectedDay.fullName,
                exercises = workoutMap[selectedDay] ?: emptyList(),
                isRestDay = isCurrentDayRest,
                onToggleRestDay = { restDaysMap[selectedDay] = it },
                colors = colors
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMPONENTES ---

@Composable
fun DaySelector(selectedDay: DayOfWeek, colors: ColorScheme, onDaySelected: (DayOfWeek) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            val isSelected = day == selectedDay

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDaySelected(day) }
                    // PrimarySoft (#2D1B59) para estados de seleção
                    .background(if (isSelected) colors.primaryContainer else Color.Transparent)
                    .padding(vertical = 12.dp, horizontal = 10.dp)
                    .width(36.dp)
            ) {
                Text(
                    text = day.shortName,
                    color = if (isSelected) colors.primary else colors.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, colors: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
        color = colors.surfaceVariant.copy(alpha = 0.5f), // Card background (#16171D)
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.FitnessCenter, null, tint = colors.primary, modifier = Modifier.size(22.dp))
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(exercise.name.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = colors.onSurface)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Padrão de status: Primary para Sets, Secondary para Reps
                    Text("${exercise.sets} SETS", color = colors.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(exercise.reps, color = colors.secondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Rounded.DragHandle, null, tint = colors.onSurfaceVariant.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun PlanHeaderSection(
    planName: String,
    colors: ColorScheme,
    onNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = "NOME DO PLANO",
            style = MaterialTheme.typography.labelSmall,
            // Texto secundário: Cinza Muted
            color = colors.onSurfaceVariant,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = planName,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Ex: PROTOCOLO TITÃ",
                    color = colors.onSurfaceVariant.copy(alpha = 0.4f)
                )
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                // Surface escuro para o container
                focusedContainerColor = colors.surfaceVariant.copy(alpha = 0.4f),
                unfocusedContainerColor = colors.surfaceVariant.copy(alpha = 0.2f),
                // Brilho Neon ao focar: Roxo Primary
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun ExerciseListSection(
    dayName: String,
    exercises: List<Exercise>,
    isRestDay: Boolean,
    onToggleRestDay: (Boolean) -> Unit,
    colors: ColorScheme
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dayName.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = colors.onSurface // Branco Puro
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "DESCANSO",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    // Verde Neon para descanso ativo
                    color = if(isRestDay) colors.tertiary else colors.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = isRestDay,
                    onCheckedChange = onToggleRestDay,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.tertiary,
                        checkedTrackColor = colors.tertiary.copy(alpha = 0.2f),
                        uncheckedThumbColor = colors.onSurfaceVariant,
                        uncheckedTrackColor = colors.outline.copy(alpha = 0.2f)
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isRestDay) {
            // Certifique-se que o RestDayPlaceholder agora aceita 'colors'
            RestDayPlaceholder(colors = colors)
        } else if (exercises.isEmpty()) {
            EmptyExercisesPlaceholder(colors = colors)
        } else {
            exercises.forEach { ExerciseItem(it, colors) }
        }
    }
}

@Composable
fun EmptyExercisesPlaceholder(colors: ColorScheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp), // Aumentei o respiro para destacar o vazio
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone com a cor de contorno/variante para parecer um "ghost" element
        Icon(
            imageVector = Icons.Rounded.FitnessCenter,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = colors.onSurfaceVariant.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "NENHUMA ATIVIDADE",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            // Texto sutil para indicar que o slot está disponível
            color = colors.onSurfaceVariant.copy(alpha = 0.3f),
            letterSpacing = 2.sp
        )

        Text(
            text = "Aguardando definição da missão",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant.copy(alpha = 0.2f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RestDayPlaceholder(colors: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = colors.tertiary.copy(alpha = 0.05f), // Verde Neon suave para descanso
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, colors.tertiary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Rounded.Spa, null, tint = colors.tertiary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(12.dp))
            Text("RECUPERAÇÃO ATIVA", fontWeight = FontWeight.Black, color = colors.tertiary)
            Text(
                "O descanso é onde o músculo realmente cresce.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}