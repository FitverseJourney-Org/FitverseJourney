package org.fitverse.project.destinations.modal_destinations.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.ui.tasks.TaskLibraryScreen
import com.example.presentation.ui.tasks.viewmodel.TasksEvent
import com.example.presentation.ui.tasks.viewmodel.TasksIntent
import com.example.presentation.ui.tasks.viewmodel.TasksViewModel

@Composable
fun TasksLibraryDestination(
    viewModel: TasksViewModel,
    taskToReplaceId: String,
    toBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val taskToReplace = uiState.missions.find { it.id == taskToReplaceId }
        ?: return

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TasksEvent.SwapSuccess -> toBack()
                else -> Unit
            }
        }
    }

    TaskLibraryScreen(
        taskToReplace  = taskToReplace,
        libraryTasks   = uiState.catalogMissions,
        onTaskSelected = { selected ->
            viewModel.onIntent(
                TasksIntent.SwapMission(
                    taskToReplaceId  = taskToReplaceId,
                    catalogMissionId = selected.id,
                )
            )
        },
        onBackClick = toBack,
    )
}
