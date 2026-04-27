package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.domain.models.local.Genero
import com.example.domain.models.auth.register.HeightUnit
import com.example.domain.models.auth.register.RegisterIntent
import com.example.domain.models.auth.register.RegisterUiState
import com.example.domain.models.auth.register.WeightUnit
import com.example.presentation.screens.ui.authentication.register.DateVisualTransformation
import com.example.presentation.screens.ui.authentication.register.HeightVisualTransformation
import com.example.presentation.screens.ui.authentication.register.WeightVisualTransformation
import com.example.presentation.screens.ui.authentication.register.components.RadioChip
import com.example.presentation.screens.ui.authentication.register.components.RegisterInputField
import com.example.presentation.screens.ui.authentication.register.components.RegisterPrimaryButton
import com.example.presentation.screens.ui.authentication.register.components.UnitSelector
import com.example.presentation.theme.RegisterDimens

@Composable
fun PerfilStep(
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

        Text(
            text  = "Personalize seu perfil de aventureiro.",
            style = MaterialTheme.typography.bodyMedium
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Spacer(Modifier.height(4.dp))

        RegisterInputField(
            value           = state.username,
            onValueChange   = { onIntent(RegisterIntent.UsernameChanged(it)) },
            label           = "NOME DE USUÁRIO",
            placeholder     = "@seu_nick",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            error           = state.usernameError,   // ValidationError?
        )

        RegisterInputField(
            value                = state.birthDate,
            onValueChange        = { onIntent(RegisterIntent.BirthDateChanged(it)) },
            label                = "DATA DE NASCIMENTO",
            placeholder          = state.birthDatePattern,
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = DateVisualTransformation(state.birthDatePattern),
            error                = state.birthDateError, // ValidationError?
        )

        RegisterInputField(
            value                = state.weight,
            onValueChange        = { onIntent(RegisterIntent.WeightChanged(it)) },
            label                = "PESO",
            placeholder          = "",
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = WeightVisualTransformation(state.weightUnit),
            error                = state.weightError, // ValidationError?
            trailingIcon         = {
                UnitSelector(
                    selected = state.weightUnit,
                    options  = WeightUnit.entries.toTypedArray(),
                    labelOf  = { it.label },
                    onSelect = { onIntent(RegisterIntent.WeightUnitChanged(it)) }
                )
            }
        )

        RegisterInputField(
            value                = state.height,
            onValueChange        = { onIntent(RegisterIntent.HeightChanged(it)) },
            label                = "ALTURA",
            placeholder          = "",
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = HeightVisualTransformation(state.heightUnit),
            error                = state.heightError, // ← ✅ corrigido (era weightError)
            trailingIcon         = {
                UnitSelector(
                    selected = state.heightUnit,
                    options  = HeightUnit.entries.toTypedArray(),
                    labelOf  = { it.label },
                    onSelect = { onIntent(RegisterIntent.HeightUnitChanged(it)) }
                )
            }
        )

        // ── Gênero ────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(RegisterDimens.itemGap)) {
            Text(
                text  = "GÊNERO",
                style = MaterialTheme.typography.labelLarge
                    .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Genero.entries.forEach { genero ->
                    RadioChip(
                        text     = genero.label,
                        selected = state.genero == genero,
                        onClick  = { onIntent(RegisterIntent.GeneroSelected(genero)) },
                    )
                }
            }
        }

        Text(
            text  = "Usado para calcular suas metas de saúde.",
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