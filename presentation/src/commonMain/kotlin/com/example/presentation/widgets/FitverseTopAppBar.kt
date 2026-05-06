package com.example.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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

    TopAppBar(
        modifier = Modifier,
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
                    text = title,
                    fontSize = 14.sp,
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
}