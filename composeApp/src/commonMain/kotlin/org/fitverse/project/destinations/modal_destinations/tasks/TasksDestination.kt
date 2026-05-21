package org.fitverse.project.destinations.modal_destinations.tasks

import TasksScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fitverse.domain.models.dashboard.tasks.TaskItem
import org.fitverse.presentation.ui.tasks.viewmodel.TasksViewModel

@Composable
fun TasksDestination(
    viewModel: TasksViewModel,
    toBack: () -> Unit,
    toLibrary: (TaskItem) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TasksScreen(
        onBack              = toBack,
        currentTasks        = uiState.missions,
        swapsRemaining      = uiState.swapsRemaining,
        onNavigateToLibrary = { task -> toLibrary(task) },
    )
}
