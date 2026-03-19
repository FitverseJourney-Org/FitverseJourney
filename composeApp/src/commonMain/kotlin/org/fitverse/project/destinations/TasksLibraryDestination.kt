package org.fitverse.project.destinations

import TaskLibraryContent
import TasksScreen
import androidx.compose.runtime.Composable
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.presentation.screens.ui.tasks.TaskLibraryScreen

@Composable
fun TasksLibraryDestination(
    toBack: () -> Unit,
    libraryTasks: List<TaskItem>,
    onTaskSelected: (TaskItem) -> Unit,
    taskToReplace: TaskItem
) {
    val mockLibraryTasks = listOf(
        TaskItem(
            id = "lib1",
            title = "Yoga de Mobilidade",
            description = "15 minutos de saudação ao sol e alongamento",
            xp = 25,
            completed = false,
            iconType = TaskIcon.WORKOUT
        ),
        TaskItem(
            id = "lib2",
            title = "Jejum de Açúcar",
            description = "Não consumir doces processados hoje",
            xp = 40,
            completed = false,
            iconType = TaskIcon.NUTRITION
        ),
        TaskItem(
            id = "lib3",
            title = "Sprint de 5km",
            description = "Corrida intensa para bater seu recorde",
            xp = 60,
            completed = false,
            iconType = TaskIcon.RUN
        ),
        TaskItem(
            id = "lib4",
            title = "Meditação Zen",
            description = "10 minutos de respiração guiada",
            xp = 15,
            completed = false,
            iconType = TaskIcon.GENERIC
        ),
        TaskItem(
            id = "lib5",
            title = "Superavit Proteico",
            description = "Consumir 30g de proteína no café da manhã",
            xp = 30,
            completed = false,
            iconType = TaskIcon.NUTRITION
        ),
        TaskItem(
            id = "lib6",
            title = "Treino de Core",
            description = "Prancha e abdominais infra",
            xp = 35,
            completed = false,
            iconType = TaskIcon.WORKOUT
        ),
        TaskItem(
            id = "lib7",
            title = "Treino de Core",
            description = "Prancha e abdominais infra",
            xp = 35,
            completed = false,
            iconType = TaskIcon.WORKOUT
        ),
        TaskItem(
            id = "lib8",
            title = "Treino de Core",
            description = "Prancha e abdominais infra",
            xp = 35,
            completed = false,
            iconType = TaskIcon.WORKOUT
        ),
        TaskItem(
            id = "lib9",
            title = "Treino de Core",
            description = "Prancha e abdominais infra",
            xp = 35,
            completed = false,
            iconType = TaskIcon.WORKOUT
        )
    )
    com.example.presentation.screens.ui.tasks.TaskLibraryScreen(
        taskToReplace = taskToReplace,
        libraryTasks = mockLibraryTasks,
        onTaskSelected = onTaskSelected,
        onBackClick = toBack
    )
}
