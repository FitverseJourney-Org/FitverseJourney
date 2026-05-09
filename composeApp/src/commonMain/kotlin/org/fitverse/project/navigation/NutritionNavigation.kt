package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.meals.MealsDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun NutritionNavigation(
    onSheetStateChange: (isOpen: Boolean) -> Unit = {}
) {
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Nutrition::class, NavRoutes.HomeFlow.Nutrition.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Nutrition
    )

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Nutrition> {
                MealsDestination(
                    onBottomSheetOpen = { isOpen -> onSheetStateChange(isOpen) }
                )
            }
        }
    )
}
