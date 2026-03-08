package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.login.state.LoginState
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.login_button_text
import fitversejourneyapp.presentation.generated.resources.login_email_label
import fitversejourneyapp.presentation.generated.resources.login_password_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginForm(
    state: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column {

        LoginTextField(
            value = { state.email },
            onValueChange = onEmailChange,
            icon = Icons.Outlined.Email,
            txtHint = stringResource(Res.string.login_email_label),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            txtPlaceholder = stringResource(Res.string.login_email_label)
        )

        Spacer(Modifier.height(8.dp))

        LoginTextField(
            value = { state.password },
            onValueChange = onPasswordChange,
            icon = Icons.Outlined.Lock,
            txtHint = stringResource(Res.string.login_password_label),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions {
                onLoginClick()
            },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (state.isPasswordVisible)
                            Icons.Outlined.Visibility
                        else
                            Icons.Outlined.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (state.isPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            txtPlaceholder = stringResource(Res.string.login_password_label),
        )

        Spacer(Modifier.height(16.dp))

        AuthDefaultButton(
            text = stringResource(Res.string.login_button_text),
            onClick = {
                onLoginClick()
            },
            isLoading = state.isLoading
        )
    }
}