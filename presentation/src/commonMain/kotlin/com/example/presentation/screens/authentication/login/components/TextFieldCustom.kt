package com.example.presentation.screens.authentication.login.components

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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldAuthentication(
    modifier: Modifier = Modifier,
    value: () -> String,
    hasSupportText: Boolean = true,
    onValueChange: (String) -> Unit,
    errorsList: List<String> = emptyList(),
    icon: ImageVector,
    txtHint: String? = null,
    txtPlaceholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    txtSuffix: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.Start
    ){
        if(txtHint != null){
            Text(
                modifier = Modifier,
                text = txtHint,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                )
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value(),
            onValueChange = {
                onValueChange(it)
            },
            shape = RoundedCornerShape(6.dp),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            placeholder = {
                Text(
                    text = txtPlaceholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.DarkGray
                    )
                )
            },
            maxLines = 1,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF044501),
                unfocusedBorderColor = Color(0xFF044701),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color(0x2C001A0F),
                cursorColor = Color.White,
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White
            ),
            isError = errorsList.isNotEmpty(),
            supportingText = {
                if(hasSupportText){
                    if (errorsList.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFF1717),
                            )
                            Text(
                                text = errorsList.getOrNull(0) ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFFF1717),
                                )
                            )
                        }
                    }
                }
            },
            suffix = {
                if(txtSuffix != null){
                    Text(
                        text = txtSuffix,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                }
            },
            trailingIcon = {
                if(trailingIcon != null){
                    trailingIcon()
                }
            },
            visualTransformation = visualTransformation
        )

    }
}