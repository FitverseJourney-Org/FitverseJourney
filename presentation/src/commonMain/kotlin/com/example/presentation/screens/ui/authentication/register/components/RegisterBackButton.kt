package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterBackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onBack, modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = "Voltar",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}