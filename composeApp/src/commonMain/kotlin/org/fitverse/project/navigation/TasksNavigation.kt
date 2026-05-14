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
import com.example.presentation.ui.tasks.viewmodel.TasksViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.modal_destinations.tasks.TasksDestination
import org.fitverse.project.destinations.modal_destinations.tasks.TasksLibraryDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun TasksNavigation(
    toBack: () -> Unit,
    modifier: Modifier
) {
    val viewModel = koinInject<TasksViewModel>()

    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.TasksFlow.TasksList::class, NavRoutes.TasksFlow.TasksList.serializer())
                    subclass(NavRoutes.TasksFlow.TasksLibrary::class, NavRoutes.TasksFlow.TasksLibrary.serializer())
                }
            }
        },
        NavRoutes.TasksFlow.TasksList
    )

    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.TasksFlow.TasksList> {
                TasksDestination(
                    viewModel = viewModel,
                    toBack    = toBack,
                    toLibrary = { task ->
                        rootBackStack.add(NavRoutes.TasksFlow.TasksLibrary(task.id))
                    },
                )
            }
            entry<NavRoutes.TasksFlow.TasksLibrary> { route ->
                TasksLibraryDestination(
                    viewModel       = viewModel,
                    taskToReplaceId = route.taskToReplaceId,
                    toBack          = {
                        if (rootBackStack.size > 1) rootBackStack.removeLastOrNull()
                        else toBack()
                    },
                )
            }
        }
    )
}
