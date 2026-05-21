package org.fitverse.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitverseTopAppBar(
    title: String,
    subtitle: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val cs = MaterialTheme.colorScheme

    Column {
        TopAppBar(
            modifier = Modifier.padding(horizontal = 5.dp),
            windowInsets = WindowInsets(0, 0, 0, 0),
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            navigationIcon = {
                FitverseIconBack(onBack = onBack)
            },
            title = {
                Column(modifier = Modifier) {
                    Text(
                        text  = title,
                        style = FVTypography.headlineSmall,
                        color = cs.onBackground,
                    )
                    subtitle?.invoke()
                }
            },
            actions = {
                actions?.invoke()
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF1C1C1C).copy(alpha = .75f),
                            Color(0xFF1C1C1C),
                            Color(0xFF1C1C1C).copy(alpha = .75f)
                        )
                    )
                )
        )
    }
}