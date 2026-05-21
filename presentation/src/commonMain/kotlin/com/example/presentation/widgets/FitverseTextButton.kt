package org.fitverse.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FitverseTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle
) {
    Text(
        modifier = modifier
            .clickable(role = Role.Button){ onClick() }
            .padding(horizontal = 5.dp, vertical = 3.dp),
        text = text,
        style = style
    )
}