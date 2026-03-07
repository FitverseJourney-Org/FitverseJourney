package com.example.presentation.screens.ui.setupLanguage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.dbLocal.language.Language
import com.example.presentation.core.utils.LanguageAvailableApp.availableLanguages
import com.example.presentation.screens.ui.setupLanguage.components.BtnChangeLanguage
import com.example.presentation.screens.ui.setupLanguage.components.LanguageItem

@Composable
fun SetupLanguageScreen(
    onConfirmLanguage: (Language) -> Unit,
    currentLanguage: Language,
    exit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        bottomBar = {
            BtnChangeLanguage(
                modifier = Modifier
                    .padding(16.dp)
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                    ),
                text = "Trocar",
                enabled = true,
                onClick = {
                    onConfirmLanguage(selectedLanguage)
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Escolha seu idioma",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onBackground
                    )

                    IconButton(onClick = exit) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = colors.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(availableLanguages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = language == selectedLanguage,
                    onClick = { selectedLanguage = language }
                )
            }
        }
    }
}
