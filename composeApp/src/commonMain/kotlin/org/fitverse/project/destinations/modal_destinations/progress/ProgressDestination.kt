package org.fitverse.project.destinations.modal_destinations.progress


import androidx.compose.runtime.Composable
import com.example.expect.DateTimeManager
import com.example.presentation.screens.ui.progress.Exercise
import com.example.presentation.screens.ui.progress.LoadProgressionPoint
import com.example.presentation.screens.ui.progress.ProgressScreen


@Composable
fun ProgressDestination(toBack: () -> Unit) {
    // --- Lista de Exercícios atualizada com o trainingSplit (Divisão ABCD) ---
    val mockExercises = listOf(
        Exercise(
            id = "1",
            name = "Supino Reto (Barra)",
            muscleGroup = "Peito",
            trainingSplit = "Treino A"
        ),
        Exercise(
            id = "2",
            name = "Levantamento Terra",
            muscleGroup = "Costas",
            trainingSplit = "Treino B"
        ),
        Exercise(
            id = "3",
            name = "Agachamento Livre",
            muscleGroup = "Pernas",
            trainingSplit = "Treino C"
        ),
        Exercise(
            id = "4",
            name = "Desenvolvimento Halteres",
            muscleGroup = "Ombros",
            trainingSplit = "Treino D"
        )
    )

    val mockSupinoProgression = listOf(
        // Janeiro - Início da Periodização
        LoadProgressionPoint(DateTimeManager.create(10, 1, 2026), 60.0, 72.0),
        LoadProgressionPoint(DateTimeManager.create(17, 1, 2026), 60.0, 73.5),
        LoadProgressionPoint(DateTimeManager.create(24, 1, 2026), 62.5, 75.0),
        LoadProgressionPoint(DateTimeManager.create(31, 1, 2026), 62.5, 76.0),

        // Fevereiro - Ajuste de Volume
        LoadProgressionPoint(DateTimeManager.create(7, 2, 2026), 65.0, 78.0),
        LoadProgressionPoint(DateTimeManager.create(14, 2, 2026), 65.0, 79.5),
        LoadProgressionPoint(DateTimeManager.create(21, 2, 2026), 65.0, 81.0),
        LoadProgressionPoint(DateTimeManager.create(28, 2, 2026), 67.5, 82.0),

        // Março - Foco em Intensidade (Rumo ao PR)
        LoadProgressionPoint(DateTimeManager.create(7, 3, 2026), 70.0, 84.0),
        LoadProgressionPoint(DateTimeManager.create(14, 3, 2026), 70.0, 85.5),
        LoadProgressionPoint(DateTimeManager.create(21, 3, 2026), 72.5, 87.0),
        LoadProgressionPoint(DateTimeManager.create(28, 3, 2026), 72.5, 88.5),

        // Abril - Deload (Queda proposital para recuperação)
        LoadProgressionPoint(DateTimeManager.create(4, 4, 2026), 60.0, 80.0),
        LoadProgressionPoint(DateTimeManager.create(11, 4, 2026), 60.0, 81.0),

        // Maio - Retomada Forte
        LoadProgressionPoint(DateTimeManager.create(2, 5, 2026), 75.0, 90.0),
        LoadProgressionPoint(DateTimeManager.create(16, 5, 2026), 75.0, 92.5),
        LoadProgressionPoint(DateTimeManager.create(30, 5, 2026), 77.5, 94.0),

        // Junho - Pico de Performance
        LoadProgressionPoint(DateTimeManager.create(6, 6, 2026), 80.0, 96.0),
        LoadProgressionPoint(DateTimeManager.create(13, 6, 2026), 82.5, 99.0),
        LoadProgressionPoint(DateTimeManager.create(20, 6, 2026), 85.0, 102.0) // Meta batida!
    )

    val mockSupinoProgressionBefore = listOf(
        // Julho 2025 - Fase Inicial (Pesos menores)
        LoadProgressionPoint(DateTimeManager.create(5, 7, 2025), 50.0, 60.0),
        LoadProgressionPoint(DateTimeManager.create(12, 7, 2025), 50.0, 61.0),
        LoadProgressionPoint(DateTimeManager.create(19, 7, 2025), 52.5, 63.0),
        LoadProgressionPoint(DateTimeManager.create(26, 7, 2025), 52.5, 64.0),

        // Agosto 2025
        LoadProgressionPoint(DateTimeManager.create(2, 8, 2025), 55.0, 66.0),
        LoadProgressionPoint(DateTimeManager.create(9, 8, 2025), 55.0, 67.5),
        LoadProgressionPoint(DateTimeManager.create(16, 8, 2025), 55.0, 68.0),
        LoadProgressionPoint(DateTimeManager.create(23, 8, 2025), 57.5, 69.0),

        // Setembro 2025
        LoadProgressionPoint(DateTimeManager.create(6, 9, 2025), 57.5, 70.0),
        LoadProgressionPoint(DateTimeManager.create(13, 9, 2025), 60.0, 72.0),
        LoadProgressionPoint(DateTimeManager.create(20, 9, 2025), 60.0, 73.0),
        LoadProgressionPoint(DateTimeManager.create(27, 9, 2025), 60.0, 74.5),

        // Outubro 2025 - Estabilização
        LoadProgressionPoint(DateTimeManager.create(4, 10, 2025), 62.5, 76.0),
        LoadProgressionPoint(DateTimeManager.create(11, 10, 2025), 62.5, 77.0),

        // Novembro 2025
        LoadProgressionPoint(DateTimeManager.create(1, 11, 2025), 65.0, 79.0),
        LoadProgressionPoint(DateTimeManager.create(15, 11, 2025), 65.0, 80.5),
        LoadProgressionPoint(DateTimeManager.create(29, 11, 2025), 67.5, 82.0),

        // Dezembro 2025 - Fechamento do ano
        LoadProgressionPoint(DateTimeManager.create(6, 12, 2025), 70.0, 84.0),
        LoadProgressionPoint(DateTimeManager.create(13, 12, 2025), 70.0, 85.0),
        LoadProgressionPoint(DateTimeManager.create(20, 12, 2025), 72.5, 87.0)
    )

    ProgressScreen(
        exercises = mockExercises,
        currentProgression = mockSupinoProgression,
        monthbeforeProgression = mockSupinoProgressionBefore,
        toBack = toBack
    )
}

