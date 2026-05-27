package org.fitverse.project.destinations.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.community.CommunityRoot
import org.fitverse.presentation.ui.community.PostDetailModel
import org.fitverse.presentation.ui.community.PostDetailScreen
import org.fitverse.presentation.ui.community.toDetailModel
import org.fitverse.presentation.ui.community.viewmodel.CommunityViewModel
import org.fitverse.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun CommunityDestination(
    viewModel:          CommunityViewModel,
    toAddPost:          () -> Unit,
    toGroupHome:        (String) -> Unit,
    onSheetStateChange: (Boolean) -> Unit = {},
    onPostDetailOpen:   (Boolean) -> Unit = {},
) {
    var selectedPost by remember { mutableStateOf<PostDetailModel?>(null) }

    LaunchedEffect(selectedPost) { onPostDetailOpen(selectedPost != null) }
    DisposableEffect(Unit) { onDispose { onPostDetailOpen(false) } }

    val defaultColor = MaterialTheme.colorScheme.primary
    val accentColor by animateColorAsState(
        targetValue   = selectedPost?.tagColor ?: defaultColor,
        animationSpec = tween(durationMillis = 600),
        label         = "community_accent",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if(selectedPost != null) DarkGamifiedDashboardBackground(accentColor = accentColor)

        AnimatedContent(
            targetState = selectedPost,
            transitionSpec = {
                if (targetState != null) {
                    (slideInVertically { it } + fadeIn(initialAlpha = 0.3f)) togetherWith
                            fadeOut(targetAlpha = 0f)
                } else {
                    fadeIn(initialAlpha = 0.3f) togetherWith
                            (slideOutVertically { it } + fadeOut(targetAlpha = 0f))
                }
            },
            label = "community_post_nav",
        ) { post ->
            if (post != null) {
                PostDetailScreen(
                    post   = post,
                    onBack = { selectedPost = null },
                )
            } else {
                CommunityRoot(
                    viewModel          = viewModel,
                    toAddPost          = toAddPost,
                    toGroupHome        = toGroupHome,
                    onSheetStateChange = onSheetStateChange,
                    onPostTapped       = { selectedPost = it.toDetailModel() },
                )
            }
        }
    }
}
