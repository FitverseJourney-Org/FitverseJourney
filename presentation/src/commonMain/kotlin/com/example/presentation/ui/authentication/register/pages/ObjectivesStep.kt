package org.fitverse.presentation.ui.authentication.register.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.fitverse.domain.models.user.NivelExperiencia
import org.fitverse.domain.models.user.Objetivo
import org.fitverse.presentation.ui.authentication.register.components.RadioChip
import org.fitverse.presentation.ui.authentication.register.components.RegisterPrimaryButton
import org.fitverse.presentation.ui.authentication.register.components.SelectionChip
import org.fitverse.presentation.ui.authentication.register.helpers.label
import org.fitverse.presentation.ui.authentication.register.states.RegisterIntent
import org.fitverse.presentation.ui.authentication.register.states.RegisterUiState
import org.fitverse.presentation.theme.RegisterDimens
import org.fitverse.presentation.ui.authentication.register.helpers.accentColor
import org.fitverse.presentation.ui.authentication.register.helpers.displayName
import org.fitverse.presentation.ui.authentication.register.helpers.frameLabel
import org.fitverse.presentation.ui.authentication.register.helpers.iconEmoji
import org.fitverse.presentation.ui.authentication.register.helpers.subtitle
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.register_button_create_account
import fitversejourneyapp.presentation.generated.resources.register_objetivos_class_frame
import fitversejourneyapp.presentation.generated.resources.register_objetivos_class_level
import fitversejourneyapp.presentation.generated.resources.register_objetivos_goal_title
import fitversejourneyapp.presentation.generated.resources.register_objetivos_level_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun GoalsStep(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography
    val shapes = MaterialTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = RegisterDimens.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(RegisterDimens.sectionGap),
    ) {
        Spacer(Modifier.height(4.dp))

        // ── Selected class summary ────────────────────────────────────────────
        state.selectedClass?.let { classe ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.medium)
                    .background(colors.surfaceVariant)
                    .border(1.dp, classe.accentColor().copy(alpha = 0.4f), shapes.medium)
                    .padding(16.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = classe.iconEmoji(), style = type.titleLarge)
                Column {
                    Text(
                        text  = stringResource(Res.string.register_objetivos_class_level, classe.displayName()),
                        style = type.titleLarge.copy(color = classe.accentColor()),
                    )
                    Text(
                        text  = stringResource(Res.string.register_objetivos_class_frame, classe.subtitle(), classe.frameLabel()),
                        style = type.bodySmall.copy(color = colors.onSurfaceVariant),
                    )
                }
            }
        }

        // ── Goals ─────────────────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(RegisterDimens.itemGap)) {
            Text(
                text  = stringResource(Res.string.register_objetivos_goal_title),
                style = type.bodyLarge.copy(color = colors.onBackground),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
            ) {
                Objetivo.entries.forEach { objetivo ->
                    SelectionChip(
                        text = objetivo.label(),
                        selected = objetivo in state.selectedObjetivos,
                        onClick = { onIntent(RegisterIntent.ObjectiveToggled(objetivo)) },
                    )
                }
            }
        }

        // ── Experience level ──────────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(RegisterDimens.itemGap)) {
            Text(
                text  = stringResource(Res.string.register_objetivos_level_title),
                style = type.bodyLarge.copy(color = colors.onBackground),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NivelExperiencia.entries.forEach { nivel ->
                    RadioChip(
                        text = nivel.label(),
                        selected = state.nivelExperiencia == nivel,
                        onClick = { onIntent(RegisterIntent.LevelSelected(nivel)) },
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        RegisterPrimaryButton(
            text = stringResource(Res.string.register_button_create_account),
            onClick = { onIntent(RegisterIntent.Submit) },
            isLoading = state.isLoading,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}