package com.example.presentation.ui.authentication.register.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RadioChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = SelectionChip(text = text, selected = selected, onClick = onClick, modifier = modifier)
