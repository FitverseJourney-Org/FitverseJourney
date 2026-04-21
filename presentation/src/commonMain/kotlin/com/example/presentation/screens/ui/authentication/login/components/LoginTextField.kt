package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expect.getCustomTextStyle

@OptIn(ExperimentalMaterial3Api::class)
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

        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            modifier = Modifier.fillMaxWidth().height(52.dp),
            value = value(),
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            textStyle = getCustomTextStyle().copy(color = colors.onSurface),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value(),
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = false,
                    placeholder = {
                        Text(
                            text = txtPlaceholder,
                            style = getCustomTextStyle().copy(color = colors.onSurfaceVariant),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colors.onSurfaceVariant
                        )
                    },
                    trailingIcon = trailingIcon,
                    suffix = {
                        txtSuffix?.let {
                            Text(text = it, color = colors.onSurfaceVariant)
                        }
                    },
                    // ✅ Aqui está o controle do padding interno
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        start = 12.dp,
                        end = 12.dp,
                        top = 0.dp,
                        bottom = 0.dp,
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedIndicatorColor = colors.primary,
                        unfocusedIndicatorColor = colors.outline,
                        errorIndicatorColor = colors.error,
                        focusedContainerColor = colors.primaryContainer.copy(alpha = 0.14f),
                        unfocusedContainerColor = colors.surface.copy(alpha = 0.02f),
                        cursorColor = colors.primary,
                        errorCursorColor = colors.error,
                        focusedLeadingIconColor = colors.primary,
                        unfocusedLeadingIconColor = colors.onSurfaceVariant,
                        errorLeadingIconColor = colors.error,
                        focusedPlaceholderColor = colors.onSurfaceVariant,
                        unfocusedPlaceholderColor = colors.onSurfaceVariant,
                        errorSupportingTextColor = colors.error
                    ),
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = colors.primary,
                                unfocusedIndicatorColor = colors.outline,
                                focusedContainerColor = colors.primaryContainer.copy(alpha = 0.14f),
                                unfocusedContainerColor = colors.surface.copy(alpha = 0.02f),
                            ),
                            shape = RoundedCornerShape(8.dp),
                        )
                    }
                )
            }
        )
    }
}