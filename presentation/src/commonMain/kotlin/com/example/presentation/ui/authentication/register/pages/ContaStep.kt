package org.fitverse.presentation.ui.authentication.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.ui.authentication.register.components.RegisterInputField
import org.fitverse.presentation.ui.authentication.register.components.RegisterPrimaryButton
import org.fitverse.presentation.ui.authentication.register.states.RegisterIntent
import org.fitverse.presentation.ui.authentication.register.states.RegisterUiState
import org.fitverse.presentation.theme.RegisterDimens
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.register_button_next
import fitversejourneyapp.presentation.generated.resources.register_conta_email_label
import fitversejourneyapp.presentation.generated.resources.register_conta_email_placeholder
import fitversejourneyapp.presentation.generated.resources.register_conta_first_name_label
import fitversejourneyapp.presentation.generated.resources.register_conta_first_name_placeholder
import fitversejourneyapp.presentation.generated.resources.register_conta_last_name_label
import fitversejourneyapp.presentation.generated.resources.register_conta_last_name_placeholder
import fitversejourneyapp.presentation.generated.resources.register_conta_password_hint
import fitversejourneyapp.presentation.generated.resources.register_conta_password_label
import fitversejourneyapp.presentation.generated.resources.register_conta_password_placeholder
import org.jetbrains.compose.resources.stringResource


@Composable
fun ContaStep(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = RegisterDimens.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(RegisterDimens.itemGap),
    ) {
        Spacer(Modifier.height(8.dp))

        RegisterInputField(
            value = state.nome,
            onValueChange = { onIntent(RegisterIntent.NomeChanged(it)) },
            label = stringResource(Res.string.register_conta_first_name_label),
            placeholder = stringResource(Res.string.register_conta_first_name_placeholder),
            error = state.nomeError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        RegisterInputField(
            value = state.lastname,
            onValueChange = { onIntent(RegisterIntent.LastnameChanged(it)) },
            label = stringResource(Res.string.register_conta_last_name_label),
            placeholder = stringResource(Res.string.register_conta_last_name_placeholder),
            error = state.lastnameError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        RegisterInputField(
            value = state.email,
            onValueChange = { onIntent(RegisterIntent.EmailChanged(it)) },
            label = stringResource(Res.string.register_conta_email_label),
            placeholder = stringResource(Res.string.register_conta_email_placeholder),
            error = state.emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        RegisterInputField(
            value = state.senha,
            onValueChange = { onIntent(RegisterIntent.PasswordChanged(it)) },
            label = stringResource(Res.string.register_conta_password_label),
            placeholder = stringResource(Res.string.register_conta_password_placeholder),
            error = state.senhaError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.senhaVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { onIntent(RegisterIntent.TogglePasswordVisibility) }) {
                    Icon(
                        imageVector = if (state.senhaVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )

        Text(
            text  = stringResource(Res.string.register_conta_password_hint),
            style = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Spacer(Modifier.weight(1f))

        RegisterPrimaryButton(
            text = stringResource(Res.string.register_button_next),
            onClick = { onIntent(RegisterIntent.Next) },
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}
