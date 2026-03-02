package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value: () -> String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    txtHint: String? = null,
    txtPlaceholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    txtSuffix: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (txtHint != null) {
            Text(
                text = txtHint,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colors.onSurfaceVariant
                )
            )
        }
        val colors = MaterialTheme.colorScheme

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value(),
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant
                )
            },

            placeholder = {
                Text(
                    text = txtPlaceholder,
                    color = colors.onSurfaceVariant
                )
            },

            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = colors.onSurface,
            ),

            colors = TextFieldDefaults.colors(

                // TEXTO
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                errorTextColor = colors.onSurface,

                // INDICATOR (equivalente à borda)
                focusedIndicatorColor = colors.primary,
                unfocusedIndicatorColor = colors.outline,
                errorIndicatorColor = colors.error,

                // CONTAINER
                focusedContainerColor = colors.primaryContainer.copy(alpha = 0.14f),
                unfocusedContainerColor = colors.surface.copy(alpha = 0.02f),

                // CURSOR
                cursorColor = colors.primary,
                errorCursorColor = colors.error,

                // ÍCONES
                focusedLeadingIconColor = colors.primary,
                unfocusedLeadingIconColor = colors.onSurfaceVariant,
                errorLeadingIconColor = colors.error,

                // PLACEHOLDER
                focusedPlaceholderColor = colors.onSurfaceVariant,
                unfocusedPlaceholderColor = colors.onSurfaceVariant,

                // SUPPORTING TEXT
                errorSupportingTextColor = colors.error
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            suffix = {
                txtSuffix?.let {
                    Text(
                        text = it,
                        color = colors.onSurfaceVariant
                    )
                }
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation
        )
    }
}