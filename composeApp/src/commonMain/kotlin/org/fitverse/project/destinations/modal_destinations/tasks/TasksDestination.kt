package org.fitverse.project.destinations.modal_destinations.tasks

import TasksScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.domain.models.dashboard.TaskIcon
import com.example.domain.models.dashboard.TaskItem

@Composable
fun TasksDestination(toBack: () -> Unit, toLibrary: () -> Unit) {
    val mockCurrentTasks = listOf(
        TaskItem(
            id = "1",
            title = "Hidratação de Elite",
            description = "Beber 2 litros de água hoje",
            xp = 15,
            completed = false,
            iconType = TaskIcon.NUTRITION
        ),
        TaskItem(
            id = "2",
            title = "Treino de Hipertrofia",
            description = "Finalizar a ficha de membros superiores",
            xp = 50,
            completed = true, // Já concluída para testar o visual esmaecido
            iconType = TaskIcon.WORKOUT
        ),
        TaskItem(
            id = "3",
            title = "Cardio do Dia",
            description = "Caminhada acelerada por 30 min",
            xp = 30,
            completed = false,
            iconType = TaskIcon.RUN
        ),
        TaskItem(
            id = "4",
            title = "Mestre da Proteína",
            description = "Bater a meta de 1.6g/kg",
            xp = 25,
            completed = false,
            iconType = TaskIcon.NUTRITION
        ),
        TaskItem(
            id = "5",
            title = "Sono Reparador",
            description = "Dormir pelo menos 7 horas",
            xp = 20,
            completed = false,
            iconType = TaskIcon.GENERIC
        )
    )

    var tasks by remember { mutableStateOf(mockCurrentTasks) }

    TasksScreen(
        onBack = toBack,
        currentTasks = tasks,
        swapsRemaining = 1,
        onToggleTask = { toggledTask ->
            // Lógica simples de checkbox para teste
            tasks = tasks.map {
                if (it.id == toggledTask.id) it.copy(completed = !it.completed) else it
            }
        },
        onNavigateToLibrary = { selectedTask ->
            // Lógica para trocar a tarefa selecionada
            toLibrary()
        }
    )
}