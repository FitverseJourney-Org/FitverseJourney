package com.example.presentation.ui.authentication.register.pages

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
import com.example.presentation.ui.authentication.register.DateVisualTransformation
import com.example.presentation.ui.authentication.register.HeightVisualTransformation
import com.example.presentation.ui.authentication.register.WeightVisualTransformation
import com.example.presentation.ui.authentication.register.components.RadioChip
import com.example.presentation.ui.authentication.register.components.RegisterInputField
import com.example.presentation.ui.authentication.register.components.RegisterPrimaryButton
import com.example.presentation.ui.authentication.register.components.UnitSelector
import com.example.presentation.ui.authentication.register.helpers.label
import com.example.presentation.ui.authentication.register.states.HeightUnit
import com.example.presentation.ui.authentication.register.states.RegisterIntent
import com.example.presentation.ui.authentication.register.states.RegisterUiState
import com.example.presentation.ui.authentication.register.states.WeightUnit
import com.example.presentation.theme.RegisterDimens
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.register_button_next
import fitversejourneyapp.presentation.generated.resources.register_perfil_birthdate_label
import fitversejourneyapp.presentation.generated.resources.register_perfil_gender_hint
import fitversejourneyapp.presentation.generated.resources.register_perfil_gender_label
import fitversejourneyapp.presentation.generated.resources.register_perfil_height_label
import fitversejourneyapp.presentation.generated.resources.register_perfil_subtitle
import fitversejourneyapp.presentation.generated.resources.register_perfil_username_label
import fitversejourneyapp.presentation.generated.resources.register_perfil_username_placeholder
import fitversejourneyapp.presentation.generated.resources.register_perfil_weight_label
import org.jetbrains.compose.resources.stringResource

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
            text  = stringResource(Res.string.register_perfil_subtitle),
            style = MaterialTheme.typography.bodyMedium
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )

        Spacer(Modifier.height(4.dp))

        RegisterInputField(
            value = state.username,
            onValueChange = { onIntent(RegisterIntent.UsernameChanged(it)) },
            label = stringResource(Res.string.register_perfil_username_label),
            placeholder = stringResource(Res.string.register_perfil_username_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            error = state.usernameError,
        )

        RegisterInputField(
            value = state.birthDate,
            onValueChange = { onIntent(RegisterIntent.BirthDateChanged(it)) },
            label = stringResource(Res.string.register_perfil_birthdate_label),
            placeholder = state.birthDatePattern,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = DateVisualTransformation(state.birthDatePattern),
            error = state.birthDateError,
        )

        RegisterInputField(
            value = state.weight,
            onValueChange = { onIntent(RegisterIntent.WeightChanged(it)) },
            label = stringResource(Res.string.register_perfil_weight_label),
            placeholder = "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = WeightVisualTransformation(state.weightUnit),
            error = state.weightError,
            trailingIcon = {
                UnitSelector(
                    selected = state.weightUnit,
                    options = WeightUnit.entries.toTypedArray(),
                    labelOf = { it.label },
                    onSelect = { onIntent(RegisterIntent.WeightUnitChanged(it)) }
                )
            }
        )

        RegisterInputField(
            value = state.height,
            onValueChange = { onIntent(RegisterIntent.HeightChanged(it)) },
            label = stringResource(Res.string.register_perfil_height_label),
            placeholder = "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = HeightVisualTransformation(state.heightUnit),
            error = state.heightError,
            trailingIcon = {
                UnitSelector(
                    selected = state.heightUnit,
                    options = HeightUnit.entries.toTypedArray(),
                    labelOf = { it.label },
                    onSelect = { onIntent(RegisterIntent.HeightUnitChanged(it)) }
                )
            }
        )

        // ── Gênero ────────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(RegisterDimens.itemGap)) {
            Text(
                text  = stringResource(Res.string.register_perfil_gender_label),
                style = MaterialTheme.typography.labelLarge
                    .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Genero.entries.forEach { genero ->
                    RadioChip(
                        text = genero.label(),
                        selected = state.genero == genero,
                        onClick = { onIntent(RegisterIntent.GeneroSelected(genero)) },
                    )
                }
            }
        }

        Text(
            text  = stringResource(Res.string.register_perfil_gender_hint),
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