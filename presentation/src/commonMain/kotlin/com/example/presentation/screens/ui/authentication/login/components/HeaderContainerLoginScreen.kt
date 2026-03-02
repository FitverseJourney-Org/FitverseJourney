package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.model.dbLocal.language.Language
import com.example.presentation.core.utils.LanguageAvailableApp.availableLanguages
import com.example.presentation.theme.CardBgDefaultColor
import com.example.presentation.theme.transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderContainerLoginScreen(
    showLanguageScreen: () -> Unit,
    currentLanguage: Language
) {
    val colors = MaterialTheme.colorScheme

    val normalizedDomain = currentLanguage.name.trim().lowercase()

    val selected = availableLanguages.firstOrNull { avail ->
        avail.name.trim().lowercase() == normalizedDomain
    } ?: availableLanguages.first()

    TopAppBar(
        modifier = Modifier.padding(horizontal = 5.dp),
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = colors.onBackground
        ),
        actions = {
            ElevatedButton(
                onClick = showLanguageScreen,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = colors.surfaceVariant,
                    contentColor = colors.onSurfaceVariant,
                ),
                contentPadding = PaddingValues(10.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp
                )
            ) {
                LanguageChooser(
                    flagRes = selected.flagRes,
                    code = selected.name
                )
            }
        }
    )
}