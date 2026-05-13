package com.example.presentation.widgets

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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors

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
                containerColor = FitverseColors.Bg,
                scrolledContainerColor = Color.Transparent
            ),
            navigationIcon = {
                FitverseIconBack(onBack = onBack)
            },
            title = {
                Column(modifier = Modifier) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = cs.onBackground,
                        letterSpacing = 1.sp
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
                            Color.Transparent,
                            FitverseColors.AccentDim2.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}