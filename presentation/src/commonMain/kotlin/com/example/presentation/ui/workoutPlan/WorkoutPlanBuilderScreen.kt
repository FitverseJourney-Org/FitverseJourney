package org.fitverse.presentation.ui.workoutPlan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fitverse.presentation.ui.workout.Exercise
import org.fitverse.presentation.ui.workoutPlan.components.DaySelector
import org.fitverse.presentation.ui.workoutPlan.components.ExerciseListSection
import org.fitverse.presentation.ui.workoutPlan.components.PlanHeaderSection
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlanBuilderViewModel
import org.fitverse.presentation.widgets.FitverseTopAppBar
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

// ─────────────────────────────────────────────────────────────────────────────
// UiState — única fonte de verdade para toda a tela
// ─────────────────────────────────────────────────────────────────────────────

data class WorkoutPlanUiState(
    val planName: String                          = "",
    val selectedDay: DayOfWeek                    = DayOfWeek.MON,
    val restDays: Map<DayOfWeek, Boolean>         = emptyMap(),
    val workoutMap: Map<DayOfWeek, List<Exercise>> = emptyMap(),
    val isTemplateSheetVisible: Boolean           = false
) {
    /** Computed properties — derivados do estado, sem lógica na UI. */
    val isCurrentDayRest: Boolean
        get() = restDays[selectedDay] ?: false

    val currentExercises: List<Exercise>
        get() = workoutMap[selectedDay] ?: emptyList()
}

// ─────────────────────────────────────────────────────────────────────────────
// UiEvent — todas as intenções do usuário tipadas
// ─────────────────────────────────────────────────────────────────────────────

sealed interface WorkoutPlanEvent {
    data class PlanNameChanged(val name: String)         : WorkoutPlanEvent
    data class DaySelected(val day: DayOfWeek)           : WorkoutPlanEvent
    data class RestDayToggled(val isRest: Boolean)       : WorkoutPlanEvent
    data class TemplateSelected(val template: WorkoutTemplate) : WorkoutPlanEvent
    data object ShowTemplateSheet                        : WorkoutPlanEvent
    data object HideTemplateSheet                        : WorkoutPlanEvent
    data object SavePlan                                 : WorkoutPlanEvent
}

// ─────────────────────────────────────────────────────────────────────────────
// SideEffect — ações pontuais que a UI precisa executar (navegação, toast, etc.)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface WorkoutPlanEffect {
    data object NavigateBack          : WorkoutPlanEffect
    data object NavigateToAddExercise : WorkoutPlanEffect
    data class ShowError(val message: String) : WorkoutPlanEffect
}


// --- MODELOS ---
@Composable
fun WorkoutPlanBuilderRoute(
    onBack: () -> Unit,
    onNavigateToAddExercise: () -> Unit,
    viewmodel: WorkoutPlanBuilderViewModel = koinViewModel(),
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    // ── Coleta efeitos pontuais (one-shot) ────────────────────────
    LaunchedEffect(Unit) {
        viewmodel.effects.collect { effect ->
            when (effect) {
                is WorkoutPlanEffect.NavigateBack          -> onBack()
                is WorkoutPlanEffect.NavigateToAddExercise -> onNavigateToAddExercise()
                is WorkoutPlanEffect.ShowError             -> {
                    // TODO: exibir Snackbar via SnackbarHostState
                }
            }
        }
    }

    WorkoutPlanBuilderScreen(
        uiState  = uiState,
        onEvent  = viewmodel::onEvent,
        onAddExercise = {
            viewmodel.onEvent(WorkoutPlanEvent.HideTemplateSheet)
            onNavigateToAddExercise()
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// TemplateBottomSheet
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bottom sheet stateless de seleção de template.
 *
 * A visibilidade é controlada externamente via [uiState.isTemplateSheetVisible].
 * Este composable apenas renderiza e dispara callbacks — nunca decide por si.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSelectTemplate: (WorkoutTemplate) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        containerColor   = colors.surface,
        dragHandle       = { BottomSheetDragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 40.dp)
        ) {
            BottomSheetHeader()

            Spacer(Modifier.height(20.dp))

            WorkoutTemplates.forEach { template ->
                TemplateCard(
                    template = template,
                    colors   = colors,
                    onClick  = { onSelectTemplate(template) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Subcomposables privados — cada um com responsabilidade única
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp)
            .size(width = 36.dp, height = 4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
    )
}

@Composable
private fun BottomSheetHeader() {
    val colors = MaterialTheme.colorScheme

    Text(
        text          = "INÍCIO RÁPIDO",
        style         = MaterialTheme.typography.titleLarge,
        fontWeight    = FontWeight.Black,
        color         = colors.onSurface,
        letterSpacing = 0.5.sp
    )

    Spacer(Modifier.height(6.dp))

    Text(
        text  = "Comece com um template comprovado e personalize depois",
        style = MaterialTheme.typography.bodyMedium,
        color = colors.onSurfaceVariant
    )
}

@Composable
private fun TemplateCard(
    template: WorkoutTemplate,
    colors: ColorScheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(colors.surfaceVariant.copy(alpha = 0.4f))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            TemplateCardHeader(template = template, colors = colors)

            Spacer(Modifier.height(6.dp))

            Text(
                text  = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            DayIndicatorRow(days = template.days, colors = colors)
        }
    }
}

@Composable
private fun TemplateCardHeader(
    template: WorkoutTemplate,
    colors: ColorScheme
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text          = template.name,
            style         = MaterialTheme.typography.labelLarge,
            fontWeight    = FontWeight.Black,
            color         = colors.tertiary,
            letterSpacing = 0.5.sp
        )

        FrequencyBadge(frequency = template.frequency, colors = colors)
    }
}

@Composable
private fun FrequencyBadge(
    frequency: String,
    colors: ColorScheme
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(colors.tertiary.copy(alpha = 0.12f))
            .border(1.dp, colors.tertiary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text       = frequency,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            color      = colors.tertiary
        )
    }
}

@Composable
private fun DayIndicatorRow(
    days: List<TemplateDay>,
    colors: ColorScheme
) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        days.forEach { day -> DayIndicator(day = day, colors = colors) }
    }
}

@Composable
private fun DayIndicator(
    day: TemplateDay,
    colors: ColorScheme
) {
    val isWorkout = day.type == TemplateDayType.WORKOUT

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                if (isWorkout) colors.tertiary.copy(alpha = 0.15f)
                else           colors.onSurfaceVariant.copy(alpha = 0.07f)
            )
            .border(
                width = 1.dp,
                color = if (isWorkout) colors.tertiary.copy(alpha = 0.5f)
                else           colors.outline.copy(alpha = 0.2f),
                shape = CircleShape
            )
    ) {
        if (isWorkout) {
            Text(
                text       = day.label,
                fontSize   = 10.sp,
                fontWeight = FontWeight.Black,
                color      = colors.tertiary
            )
        } else {
            Text(text = "🔥", fontSize = 11.sp)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanBuilderScreen(
    uiState: WorkoutPlanUiState,
    onEvent: (WorkoutPlanEvent) -> Unit,
    onAddExercise: () -> Unit
) {
    val colors     = MaterialTheme.colorScheme
    val scope      = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ── BottomSheet — controlado via uiState.isTemplateSheetVisible ──
    if (uiState.isTemplateSheetVisible) {
        TemplateBottomSheet(
            sheetState       = sheetState,
            onDismiss        = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onEvent(WorkoutPlanEvent.HideTemplateSheet)
                }
            },
            onSelectTemplate = { template ->
                onEvent(WorkoutPlanEvent.TemplateSelected(template))
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopAppBar(
                title   = "MONTAR TREINO",
                onBack  = { onEvent(WorkoutPlanEvent.SavePlan) },
                actions = {
                    TextButton(onClick = { onEvent(WorkoutPlanEvent.SavePlan) }) {
                        Text(
                            text       = "SALVAR",
                            color      = colors.secondary,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                enter = fadeIn(),
                exit  = fadeOut(),
                visible = !uiState.isCurrentDayRest
            ) {
                ExtendedFloatingActionButton(
                    onClick        = onAddExercise,
                    containerColor = colors.primary,
                    contentColor   = Color.Black,
                    shape          = RoundedCornerShape(16.dp),
                    icon           = { Icon(Icons.Rounded.Add, contentDescription = null) },
                    text           = {
                        Text(
                            text       = "ADICIONAR EXERCÍCIO",
                            fontWeight = FontWeight.Black
                        )
                    }
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
            PlanHeaderSection(
                planName = uiState.planName,
                colors   = colors,
                onNameChange = { onEvent(WorkoutPlanEvent.PlanNameChanged(it)) }
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text          = "CRONOGRAMA SEMANAL",
                style         = MaterialTheme.typography.labelLarge,
                color         = colors.secondary,
                modifier      = Modifier.padding(horizontal = 20.dp),
                fontWeight    = FontWeight.Black,
                letterSpacing = 1.sp
            )

            DaySelector(
                selectedDay = uiState.selectedDay,
                colors = colors,
                onDaySelected = { onEvent(WorkoutPlanEvent.DaySelected(it)) }
            )

            ExerciseListSection(
                dayName = uiState.selectedDay.fullName,
                exercises = uiState.currentExercises,
                isRestDay = uiState.isCurrentDayRest,
                onToggleRestDay = { onEvent(WorkoutPlanEvent.RestDayToggled(it)) },
                onUseTemplate = {
                    onEvent(WorkoutPlanEvent.ShowTemplateSheet)
                    scope.launch { sheetState.show() }
                },
                colors = colors
            )

            Spacer(Modifier.height(100.dp))
        }
    }
}
