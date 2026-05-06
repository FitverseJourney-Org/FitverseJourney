package com.example.presentation.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.domain.models.validations.ValidationType
import com.example.presentation.theme.RegisterDimens

@Composable
fun RegisterInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    error: ValidationType? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography

    val errorMessage = error?.toMessage()

    val borderColor by animateColorAsState(
        targetValue = when {
            error != null      -> colors.error
            value.isNotEmpty() -> colors.primary.copy(alpha = 0.5f)
            else               -> colors.outline
        },
        label = "inputBorder",
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text  = label,
            style = type.labelLarge.copy(color = colors.onSurfaceVariant),
        )
        BasicTextField(
            value                = value,
            onValueChange        = onValueChange,
            textStyle            = type.bodyLarge.copy(color = colors.onBackground),
            cursorBrush          = SolidColor(colors.primary),
            keyboardOptions      = keyboardOptions,
            keyboardActions      = keyboardActions,
            visualTransformation = visualTransformation,
            singleLine           = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(RegisterDimens.inputHeight)
                .clip(MaterialTheme.shapes.small)
                .background(colors.surfaceVariant)
                .border(1.dp, borderColor, MaterialTheme.shapes.small),
        ) { inner ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text  = placeholder,
                            style = type.bodyLarge.copy(color = colors.onSurfaceVariant),
                        )
                    }
                    inner()
                }
                trailingIcon?.invoke()
            }
        }
        if (errorMessage != null) {
            Text(
                text  = errorMessage,
                style = type.bodySmall.copy(color = colors.error),
            )
        }
    }
}
