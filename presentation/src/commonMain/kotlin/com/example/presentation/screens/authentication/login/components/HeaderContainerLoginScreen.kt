package com.example.presentation.screens.authentication.login.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
     //Normaliza o nome do enum para comparação (ex: "PT" -> "pt")
    val normalizedDomain = currentLanguage.name.trim().lowercase()

    // procura a linguagem correspondente na sua lista de languages disponíveis
    val selected = availableLanguages.firstOrNull { avail ->
        // compara por name (ex.: "pt", "en") ou por uma propriedade alternativa se existir
        avail.name.trim().lowercase() == normalizedDomain
    } ?: availableLanguages.first() // fallback seguro

    TopAppBar(
        modifier = Modifier.padding(horizontal = 5.dp),
        title = {  },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = transparent
        ),
        actions = {
            ElevatedButton(
                onClick = { showLanguageScreen() },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = CardBgDefaultColor,
                ),
                contentPadding = PaddingValues(10.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                LanguageChooser(
                    flagRes = selected.flagRes,
                    code = selected.name
                )
            }
        }
    )
}