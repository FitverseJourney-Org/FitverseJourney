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
import com.example.domain.models.dashboard.tasks.TaskIcon
import com.example.domain.models.dashboard.tasks.TaskItem
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
            entry<NavRoutes.TasksFlow.TasksList>{
                val viewModel = koinInject<TasksViewModel>()
                TasksDestination(
                    viewModel = viewModel,
                    toBack    = { toBack() },
                    toLibrary = { rootBackStack.add(NavRoutes.TasksFlow.TasksLibrary) },
                )
            }
            entry<NavRoutes.TasksFlow.TasksLibrary>{
                TasksLibraryDestination(
                    toBack = {
                        toBack()
                    },
                    libraryTasks = listOf(),
                    onTaskSelected = {},
                    taskToReplace = TaskItem(
                        id = "1",
                        title = "Exercícios de Agilidade",
                        description = "Treine sua agilidade e precisão",
                        iconType = TaskIcon.RUN,
                        xp = 100,
                        completed = false,
                    )
                )
            }
        }
    )
}