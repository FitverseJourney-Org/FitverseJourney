package com.example.presentation.components.snackbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    snackbarType: SnackbarType,
) {
    SnackbarHost(
        hostState = snackbarHostState
    ) { data ->
        val (containerColor, contentColor) = when (snackbarType) {
            SnackbarType.SUCCESS -> Color(0xFF1E6F43) to Color.White
            SnackbarType.ERROR -> Color(0xFF8B1D18) to Color.White
            SnackbarType.INFO -> Color(0xFF1E1E1E) to Color.White
        }

        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            Text(text = data.visuals.message)
        }
    }
}