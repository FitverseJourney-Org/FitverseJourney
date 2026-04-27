package com.example.presentation.screens.ui.authentication.register.pages

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
import com.example.domain.models.auth.register.RegisterIntent
import com.example.domain.models.auth.register.RegisterUiState
import com.example.presentation.screens.ui.authentication.register.components.RegisterInputField
import com.example.presentation.screens.ui.authentication.register.components.RegisterPrimaryButton
import com.example.presentation.theme.RegisterDimens


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
            value           = state.nome,
            onValueChange   = { onIntent(RegisterIntent.NomeChanged(it)) },
            label           = "FIRST NAME",
            placeholder     = "Joe",
            error           = state.nomeError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        RegisterInputField(
            value           = state.lastname,
            onValueChange   = { onIntent(RegisterIntent.LastnameChanged(it)) },
            label           = "LAST NAME",
            placeholder     = "perplex",
            error           = state.lastnameError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        RegisterInputField(
            value           = state.email,
            onValueChange   = { onIntent(RegisterIntent.EmailChanged(it)) },
            label           = "E-MAIL",
            placeholder     = "seu@email.com",
            error           = state.emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )

        RegisterInputField(
            value                = state.senha,
            onValueChange        = { onIntent(RegisterIntent.PasswordChanged(it)) },
            label                = "SENHA",
            placeholder          = "Mínimo 8 caracteres",
            error                = state.senhaError,
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.senhaVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { onIntent(RegisterIntent.TogglePasswordVisibility) }) {
                    Icon(
                        imageVector        = if (state.senhaVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )

        Text(
            text  = "Use letras, números e símbolos",
            style = MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Spacer(Modifier.weight(1f))

        RegisterPrimaryButton(
            text     = "PRÓXIMO →",
            onClick  = { onIntent(RegisterIntent.Next) },
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}
