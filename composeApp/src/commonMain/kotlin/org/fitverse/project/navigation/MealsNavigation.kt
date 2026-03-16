package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.routes.NavRoutes
import kotlin.collections.listOf

@Composable
fun MealsNavigation() {
    val mealsBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.MealsFlow.WorkoutSession::class, NavRoutes.MealsFlow.WorkoutSession.serializer())
                }
            }
        },
        NavRoutes.MealsFlow.WorkoutSession
    )

    NavDisplay(
        modifier = Modifier,
        backStack = mealsBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.MealsFlow.WorkoutSession>{
                // TODO: Add MealsSessionDestination
            }
        }
    )
}