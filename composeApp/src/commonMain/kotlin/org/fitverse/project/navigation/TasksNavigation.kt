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
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.TasksDestination
import org.fitverse.project.destinations.TasksLibraryDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun TasksNavigation(
    toBack: () -> Unit
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
        modifier = Modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.TasksFlow.TasksList>{
                TasksDestination(
                    toBack = {
                        toBack()
                    },
                    toLibrary = {
                        rootBackStack.add(NavRoutes.TasksFlow.TasksLibrary)
                    }
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