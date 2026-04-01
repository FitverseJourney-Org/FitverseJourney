package org.fitverse.project.destinations.homepage.meals


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.meals.FoodEntry
import com.example.presentation.screens.ui.meals.MealGroup
import com.example.presentation.screens.ui.meals.MealPeriod
import com.example.presentation.screens.ui.meals.PlanMealsListScreenPro
import com.example.presentation.screens.ui.planMeals.MealPlanScreenState
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground

@Composable
fun MealsDestination(
    toAddMeal: (MealPeriod) -> Unit
) {
    val mockMeals = listOf(
        MealGroup(
            id = "1",
            name = "Café da Manhã",
            foods = listOf(
                FoodEntry("f1", "Ovos Mexidos", "3 unidades", 210, 18, 2, 15),
                FoodEntry("f2", "Pão de Forma Integral", "2 fatias", 120, 4, 22, 1),
                FoodEntry("f3", "Café com Leite", "200ml", 60, 3, 5, 3)
            )
        ),
        MealGroup(
            id = "2",
            name = "Almoço",
            foods = listOf(
                FoodEntry("f4", "Peito de Frango Grelhado", "150g", 245, 46, 0, 5),
                FoodEntry("f5", "Arroz Branco", "100g", 130, 2, 28, 0),
                FoodEntry("f6", "Feijão Carioca", "100g", 76, 5, 14, 1)
            )
        ),
        MealGroup(
            id = "3",
            name = "Lanche da Tarde",
            foods = listOf(
                FoodEntry("f7", "Whey Protein", "30g", 110, 24, 2, 1),
                FoodEntry("f8", "Banana Prata", "1 unidade", 90, 1, 23, 0)
            )
        ),
        MealGroup(
            id = "4",
            name = "Jantar",
            foods = emptyList() // Para testar o estado vazio "Toque para adicionar"
        ),
        MealGroup(
            id = "5",
            name = "Ceia",
            foods = listOf(
                FoodEntry("f9", "Iogurte Natural", "170g", 120, 7, 9, 6),
                FoodEntry("f10", "Pasta de Amendoim", "15g", 90, 4, 3, 8)
            )
        )
    )

    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        PlanMealsListScreenPro(
            state = MealPlanScreenState(),
            onBackClick = {},
            onEditGoalClick = {},
            onAddFoodClick = { toAddMeal(MealPeriod.BREAKFAST) },
            meals = mockMeals
        )
    }
}