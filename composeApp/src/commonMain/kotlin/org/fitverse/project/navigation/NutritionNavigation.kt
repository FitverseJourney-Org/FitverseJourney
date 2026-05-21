package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.fitverse.presentation.ui.meals.viewmodel.AddManualFoodViewModel
import org.fitverse.presentation.ui.meals.viewmodel.MealsViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.meals.AddManualFoodDestination
import org.fitverse.project.destinations.meals.MealsDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun NutritionNavigation(
    onSheetStateChange: (isOpen: Boolean) -> Unit = {},
    onSubScreenChange: (Boolean) -> Unit = {},
    modifier: Modifier,
    subScreenModifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Nutrition::class,        NavRoutes.HomeFlow.Nutrition.serializer())
                    subclass(NavRoutes.NutritionAddManualFood::class,    NavRoutes.NutritionAddManualFood.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Nutrition
    )

    val isSubScreen = backStack.lastOrNull() != NavRoutes.HomeFlow.Nutrition

    LaunchedEffect(isSubScreen) { onSubScreenChange(isSubScreen) }

    DisposableEffect(Unit) { onDispose { onSubScreenChange(false) } }

    val mealsViewModel     = koinInject<MealsViewModel>()
    val addFoodViewModel   = koinInject<AddManualFoodViewModel>()

    NavDisplay(
        modifier = if (isSubScreen) subScreenModifier else modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Nutrition> {
                MealsDestination(
                    viewModel                 = mealsViewModel,
                    onBottomSheetOpen         = { isOpen -> onSheetStateChange(isOpen) },
                    onNavigateToAddManualFood = { mealId, mealName ->
                        backStack.add(NavRoutes.NutritionAddManualFood(mealId = mealId, mealName = mealName))
                    },
                )
            }
            entry<NavRoutes.NutritionAddManualFood> { key ->
                AddManualFoodDestination(
                    mealId    = key.mealId,
                    mealName  = key.mealName,
                    viewModel = addFoodViewModel,
                    onBack    = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}
