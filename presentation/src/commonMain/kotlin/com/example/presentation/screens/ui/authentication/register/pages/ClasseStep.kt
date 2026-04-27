package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.models.local.ClassType
import com.example.domain.models.auth.register.RegisterIntent
import com.example.domain.models.auth.register.RegisterUiState
import com.example.presentation.screens.ui.authentication.register.components.ClassCard
import com.example.presentation.screens.ui.authentication.register.components.RegisterPrimaryButton
import com.example.presentation.theme.RegisterDimens

@Composable
fun ClasseStep(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text     = "Seu arquétipo define sua jornada — não sua limitação.\nVocê pode mudar depois.",
            style    = type.bodyMedium.copy(color = colors.onSurfaceVariant),
            modifier = Modifier.padding(horizontal = RegisterDimens.screenHorizontal),
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                horizontal = RegisterDimens.screenHorizontal,
                vertical   = 4.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(ClassType.entries) { classType ->
                ClassCard(
                    classType  = classType,
                    isSelected = state.selectedClass == classType,
                    onSelect   = { onIntent(RegisterIntent.ClassSelected(classType)) },
                )
            }
            item { Spacer(Modifier.height(4.dp)) }
        }

        Text(
            text     = "Você poderá trocar de classe no nível 10.",
            style    = type.bodySmall.copy(color = colors.onSurfaceVariant),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
        )

        RegisterPrimaryButton(
            text     = "CONFIRMAR CLASSE",
            onClick  = { onIntent(RegisterIntent.ConfirmClass) },
            enabled  = state.selectedClass != null,
            modifier = Modifier
                .padding(horizontal = RegisterDimens.screenHorizontal)
                .padding(bottom = 32.dp),
        )
    }
}