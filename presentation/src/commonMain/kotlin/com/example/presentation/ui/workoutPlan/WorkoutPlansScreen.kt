@file:OptIn(ExperimentalMaterial3Api::class)

package org.fitverse.presentation.ui.workoutPlan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlansViewModel
import org.fitverse.presentation.widgets.FitverseTopAppBar
import org.fitverse.domain.models.workoutPlan.WorkoutPlan
import org.fitverse.presentation.ui.workoutPlan.state.WorkoutPlansUiState
import org.koin.compose.koinInject


/**
 * Root composable that:
 *  1. Creates / retrieves the ViewModel (with mock dependencies)
 *  2. Collects the UI state as Compose State
 *  3. Passes state + callbacks down to the pure [WorkoutPlansScreen]
 *
 * In a real app, DI (Koin / Hilt) would provide the ViewModel and
 * repository; here everything is wired manually for demo purposes.
 */
@Composable
fun WorkoutPlansRoot(
    onBack: () -> Unit = {},
    onEditPlan: (String) -> Unit = {},
    onNewPlan: () -> Unit = {}
) {
    val viewModel = koinInject<WorkoutPlansViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    WorkoutPlansScreen(
        uiState                = uiState,
        onBack                 = onBack,
        onShowCreateSheet      = viewModel::onShowCreateSheet,
        onDismissCreateSheet   = viewModel::onDismissCreateSheet,
        onCreateWithAI         = {

        },
        onCreateManually       = {
            viewModel.onCreateManually()
            onNewPlan()
        },
        onSelectPlan           = viewModel::onSelectPlan,
        onDismissSelection     = viewModel::onDismissSelection,
        onActivatePlan         = viewModel::onActivatePlan,
        onEditPlan             = { plan ->
            viewModel.onEditPlan(plan)
            onEditPlan(plan.id)
        },
        onRequestDeletePlan    = viewModel::onRequestDeletePlan,
        onConfirmDelete        = viewModel::onConfirmDelete,
        onCancelDelete         = viewModel::onCancelDelete
    )
}

@Composable
fun WorkoutPlansScreen(
    uiState: WorkoutPlansUiState,
    onBack: () -> Unit,
    onShowCreateSheet: () -> Unit,
    onDismissCreateSheet: () -> Unit,
    onCreateWithAI: () -> Unit,
    onCreateManually: () -> Unit,
    onSelectPlan: (WorkoutPlan) -> Unit,
    onDismissSelection: () -> Unit,
    onActivatePlan: (String) -> Unit,
    onEditPlan: (WorkoutPlan) -> Unit,
    onRequestDeletePlan: (WorkoutPlan) -> Unit,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = cs.background,
        // ── Top bar ───────────────────────────────────────────────
        topBar = {
            FitverseTopAppBar(
                title = "PLANOS DE TREINO",
                subtitle = {
                    Text(
                        text = "Máx. 2 planos · gerencie sua jornada",
                        color = FitColors.TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBack = onBack
            )
        },
        floatingActionButton = {
            val isAtLimit = uiState.isAtLimit
            ExtendedFloatingActionButton(
                onClick = { if (!isAtLimit) onShowCreateSheet() },
                containerColor = if (isAtLimit) FitColors.Surface else FitColors.Accent,
                contentColor = if (isAtLimit) FitColors.TextMuted else FitColors.Bg,
                shape = RoundedCornerShape(WorkoutDimensions.ButtonCornerRadius),
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = {
                    Text(
                        text = if (isAtLimit) "LIMITE ${uiState.planCount}" else "CRIAR PLANO",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                },
            )
        },
    ) { innerPadding ->

        // ── Body ─────────────────────────────────────────────────
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {

            // ── Scrollable content ────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = WorkoutDimensions.ScreenPadding)
            ) {
                Spacer(Modifier.height(20.dp))

                if (uiState.plans.isEmpty()) {
                    NoWorkoutPlanCard(onCreatePlan = onShowCreateSheet)
                } else {
                    // Active plan featured card
                    if (uiState.activePlan != null) {
                        ActivePlanCard(plan = uiState.activePlan!!)
                        Spacer(Modifier.height(24.dp))
                    }

                    // Section header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MEUS PLANOS",
                            color = FitColors.TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = uiState.planCount,
                            color = FitColors.TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Inactive plans + placeholder OR limit banner
                    if (!uiState.isAtLimit) {
                        uiState.inactivePlans.forEach { plan ->
                            InactivePlanCard(
                                plan = plan,
                                isSelected = uiState.selectedPlan?.id == plan.id,
                                onSelect = { onSelectPlan(plan) },
                                onActivate = { onActivatePlan(plan.id) }
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                        CreatePlanPlaceholder(onClick = onShowCreateSheet)
                    } else {
                        uiState.inactivePlans.forEach { plan ->
                            InactivePlanCard(
                                plan = plan,
                                isSelected = uiState.selectedPlan?.id == plan.id,
                                onSelect = { onSelectPlan(plan) },
                                onActivate = { onActivatePlan(plan.id) }
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                        LimitReachedBanner()
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // ── Action menu overlay ──────────────────────────────
            AnimatedVisibility(
                visible = uiState.selectedPlan != null,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                // Semi-transparent scrim — tap to dismiss
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FitColors.Scrim.copy(alpha = 0.4f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDismissSelection() }
                )

                // Floating action buttons anchored to bottom-end
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 24.dp)
                ) {
                    uiState.selectedPlan?.let { plan ->
                        PlanActionBottomMenu(
                            onEdit   = { onEditPlan(plan) },
                            onDelete = { onRequestDeletePlan(plan) },
                            onDismiss = onDismissSelection
                        )
                    }
                }
            }

            // ── Delete confirmation dialog ────────────────────────
            if (uiState.showDeleteDialog) {
                DeletePlanDialog(
                    planName  = uiState.planToDelete?.name.orEmpty(),
                    onConfirm = onConfirmDelete,
                    onDismiss = onCancelDelete
                )
            }
        }
    }

    // Create plan bottom sheet — rendered above Scaffold so it covers bottomBar
    if (uiState.showCreateBottomSheet) {
        CreatePlanBottomSheet(
            onCreateWithAI   = onCreateWithAI,
            onCreateManually = onCreateManually,
            onDismiss        = onDismissCreateSheet
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WorkoutTemplate
// ─────────────────────────────────────────────────────────────────────────────

enum class TemplateDayType { WORKOUT, REST }

data class TemplateDay(
    val label: String,
    val type: TemplateDayType
)

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val frequency: String,
    val description: String,
    val days: List<TemplateDay>   // sempre 7 entradas, ordem = DayOfWeek.ordered
) {
    /** Converte o template em um mapa de dias de descanso pronto para a UI. */
    fun toRestDaysMap(): Map<DayOfWeek, Boolean> =
        DayOfWeek.ordered.zip(days).associate { (dow, day) ->
            dow to (day.type == TemplateDayType.REST)
        }
}

// ─────────────────────────────────────────────────────────────────────────────
// Catálogo
// ─────────────────────────────────────────────────────────────────────────────

val WorkoutTemplates: List<WorkoutTemplate> = listOf(
    WorkoutTemplate(
        id          = "push_pull_legs",
        name        = "PUSH · PULL · LEGS",
        frequency   = "4×/sem",
        description = "Treino dividido em 3 dias por grupos musculares",
        days        = listOf(
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("T", TemplateDayType.WORKOUT),
            TemplateDay("Q", TemplateDayType.REST),
            TemplateDay("Q", TemplateDayType.WORKOUT),
            TemplateDay("S", TemplateDayType.REST),
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("D", TemplateDayType.REST)
        )
    ),
    WorkoutTemplate(
        id          = "abc_classico",
        name        = "ABC · CLÁSSICO",
        frequency   = "4×/sem",
        description = "3 treinos rotacionados — peito/costas/pernas",
        days        = listOf(
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("T", TemplateDayType.WORKOUT),
            TemplateDay("Q", TemplateDayType.REST),
            TemplateDay("Q", TemplateDayType.WORKOUT),
            TemplateDay("S", TemplateDayType.REST),
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("D", TemplateDayType.REST)
        )
    ),
    WorkoutTemplate(
        id          = "upper_lower",
        name        = "UPPER · LOWER",
        frequency   = "4×/sem",
        description = "4 dias alternando membros superiores e inferiores",
        days        = listOf(
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("T", TemplateDayType.WORKOUT),
            TemplateDay("Q", TemplateDayType.REST),
            TemplateDay("Q", TemplateDayType.WORKOUT),
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("S", TemplateDayType.REST),
            TemplateDay("D", TemplateDayType.REST)
        )
    ),
    WorkoutTemplate(
        id          = "full_body",
        name        = "FULL BODY · 3×SEM",
        frequency   = "3×/sem",
        description = "Treino completo, ideal para iniciantes",
        days        = listOf(
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("T", TemplateDayType.REST),
            TemplateDay("Q", TemplateDayType.WORKOUT),
            TemplateDay("Q", TemplateDayType.REST),
            TemplateDay("S", TemplateDayType.WORKOUT),
            TemplateDay("S", TemplateDayType.REST),
            TemplateDay("D", TemplateDayType.REST)
        )
    )
)

/**
 * Custom DayOfWeek para evitar dependência de java.time,
 * garantindo compatibilidade total com KMP (Android + iOS).
 */

