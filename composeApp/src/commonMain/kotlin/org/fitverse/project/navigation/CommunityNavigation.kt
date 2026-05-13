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
import com.example.presentation.ui.community.viewmodel.CommunityViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.community.AddPostDestination
import org.fitverse.project.destinations.community.CommunityDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun CommunityNavigation(
    onSubScreenChange:  (Boolean) -> Unit = {},
    onSheetStateChange: (Boolean) -> Unit = {},
    modifier:           Modifier,
    subScreenModifier:  Modifier = Modifier,
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
        modifier = if (isSubScreen) subScreenModifier else modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Community> {
                val viewModel = koinInject<CommunityViewModel>()
                CommunityDestination(
                    viewModel          = viewModel,
                    toAddPost          = { backStack.add(NavRoutes.HomeFlow.SubFlow.AddPost) },
                    onSheetStateChange = onSheetStateChange,
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
