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
fun WorkoutNavigation() {
    val workoutBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.WorkoutFlow.WorkoutSession::class, NavRoutes.WorkoutFlow.WorkoutSession.serializer())
                }
            }
        },
        NavRoutes.WorkoutFlow.WorkoutSession
    )

    NavDisplay(
        modifier = Modifier,
        backStack = workoutBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.WorkoutFlow.WorkoutSession>{
               // TODO: Add WorkoutSessionDestination
            }
        }
    )
}