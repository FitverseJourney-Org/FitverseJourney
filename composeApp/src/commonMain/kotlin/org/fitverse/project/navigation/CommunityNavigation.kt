package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.community.AddPostDestination
import org.fitverse.project.destinations.homepage.community.CommunityDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun CommunityNavigation(
    onSubScreenChange: (Boolean) -> Unit = {}
) {
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Community::class,          NavRoutes.HomeFlow.Community.serializer())
                    subclass(NavRoutes.HomeFlow.SubFlow.AddPost::class,    NavRoutes.HomeFlow.SubFlow.AddPost.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Community
    )

    val isSubScreen = backStack.lastOrNull() != NavRoutes.HomeFlow.Community

    LaunchedEffect(isSubScreen) { onSubScreenChange(isSubScreen) }

    DisposableEffect(Unit) { onDispose { onSubScreenChange(false) } }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Community> {
                CommunityDestination(
                    toAddPost = { backStack.add(NavRoutes.HomeFlow.SubFlow.AddPost) }
                )
            }
            entry<NavRoutes.HomeFlow.SubFlow.AddPost> {
                AddPostDestination(
                    toBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
