package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.login.state.LoginState
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.screens.widgets.FitverseSocialButton
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_apple
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_facebook
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_google
import fitversejourneyapp.presentation.generated.resources.ico_apple
import fitversejourneyapp.presentation.generated.resources.ico_facebook
import fitversejourneyapp.presentation.generated.resources.ico_google
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
    onLoginClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
) {
    val cs = MaterialTheme.colorScheme

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

        FitVerseButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.login_button_text),
            topColor = cs.primary,
            edgeColor = cs.outline, // Ou uma cor mais escura que o primary
            textColor = cs.onPrimary,
            isLoading = state.isLoading,
            onClick = {
                onLoginClick()
            }
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            HorizontalDivider(
                color = cs.outline,
                modifier = Modifier.weight(1f)
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "OR",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.outline
            )
            HorizontalDivider(
                color = cs.outline,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(24.dp))
        // Botões Sociais
        FitverseSocialButton(Res.drawable.ico_google, stringResource(resource = Res.string.home_auth_btn_google), onGoogleSignIn)
      }
}