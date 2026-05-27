package org.fitverse.project.destinations.activity

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fitverse.presentation.ui.activity.ActivityDetailScreen
import org.fitverse.presentation.ui.activity.ActivityScreen
import org.fitverse.presentation.ui.activity.RecentActivity
import org.fitverse.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun ActivityDestination(modifier: Modifier = Modifier) {
    var selectedActivity by remember { mutableStateOf<RecentActivity?>(null) }
    var isRefreshing     by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val defaultColor = MaterialTheme.colorScheme.primary
    val targetColor  = selectedActivity?.type?.color ?: defaultColor

    // Transição suave de cor ao entrar/sair do detalhe de cada modalidade
    val accentColor by animateColorAsState(
        targetValue  = targetColor,
        animationSpec = tween(durationMillis = 600),
        label        = "activity_accent",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        DarkGamifiedDashboardBackground(accentColor = accentColor)

        AnimatedContent(
            targetState = selectedActivity,
            transitionSpec = {
                if (targetState != null) {
                    (slideInVertically { it } + fadeIn(initialAlpha = 0.3f)) togetherWith
                    fadeOut(targetAlpha = 0f)
                } else {
                    fadeIn(initialAlpha = 0.3f) togetherWith
                    (slideOutVertically { it } + fadeOut(targetAlpha = 0f))
                }
            },
            label = "activity_nav",
        ) { activity ->
            if (activity != null) {
                ActivityDetailScreen(
                    modifier = modifier,
                    activity = activity,
                    onBack   = { selectedActivity = null },
                )
            } else {
                ActivityScreen(
                    modifier         = modifier,
                    isRefreshing     = isRefreshing,
                    onRefresh        = {
                        scope.launch {
                            isRefreshing = true
                            delay(1000)
                            isRefreshing = false
                        }
                    },
                    onStartActivity  = {},
                    onActivityTapped = { selectedActivity = it },
                )
            }
        }
    }
}
