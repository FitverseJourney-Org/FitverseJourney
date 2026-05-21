package org.fitverse.presentation.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FitVerseSpacer(
    vertical: Boolean = false,
    horizontal: Boolean = false,
    value: Dp,
) {
    if(vertical){
        Spacer(modifier = Modifier.height(value))
    }

    if(horizontal){
        Spacer(modifier = Modifier.width(value))
    }
}