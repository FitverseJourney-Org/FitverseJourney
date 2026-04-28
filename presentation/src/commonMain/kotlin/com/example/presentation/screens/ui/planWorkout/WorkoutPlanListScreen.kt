package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.models.workout.workout_plan.WorkoutScreenState
import com.example.presentation.screens.ui.dashboard.components.SectionHeader
import com.example.presentation.screens.ui.planWorkout.components.AdaptiveWorkoutPlanCard
import com.example.presentation.screens.ui.planWorkout.components.CreatePlanMethodDialog
import com.example.presentation.screens.ui.planWorkout.components.NoActivePlanCard
import com.example.presentation.screens.ui.planWorkout.components.WorkoutOverviewCard
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanListScreen(
    state: WorkoutScreenState,
    onBack: () -> Unit,
    onSelectedPlan: () -> Unit,
    // Alterado: onNewPlan agora lida com o tipo escolhido, mas a tela gerencia o dialog
    onNavigateToManualCreation: () -> Unit,
    onNavigateToAiCreation: () -> Unit,
    onActivatePlan: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    // Controle de estado do Dialog
    var showCreateDialog by remember { mutableStateOf(false) }

    if (showCreateDialog) {
        CreatePlanMethodDialog(
            onDismiss = { showCreateDialog = false },
            onManualSelected = {
                showCreateDialog = false
                onNavigateToManualCreation()
            },
            onAiSelected = {
                showCreateDialog = false
                onNavigateToAiCreation()
            }
        )
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FitverseTopAppBar(title = "PLANOS DE TREINO", onBack = onBack)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = cs.primary,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("NOVO PLANO", fontWeight = FontWeight.Black) }
            )
        },
        containerColor = cs.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            item {
                SectionHeader(title = "PLANO ATIVO")
                FitVerseSpacer(vertical = true, value = 12.dp)

                if (state.activePlan != null) {
                    WorkoutOverviewCard(state = state)
                } else {
                    NoActivePlanCard(onCreateClick = { showCreateDialog = true })
                }
            }

            item {
                SectionHeader(title = "Planos Disponiveis")
                FitVerseSpacer(vertical = true, value = 12.dp)
            }

            items(state.availablePlans) { plan ->
                AdaptiveWorkoutPlanCard(plan = plan, onClick = onSelectedPlan)
            }
        }
    }
}

