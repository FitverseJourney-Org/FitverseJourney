package org.fitverse.project.destinations.modal_destinations.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import org.fitverse.presentation.ui.LanguageViewModel
import org.fitverse.presentation.ui.setupLanguage.SetupLanguageScreen
import org.fitverse.presentation.utils.LanguageAvailableApp
import org.koin.compose.koinInject

@Composable
fun LanguageDestination(
    modifier: Modifier,
    toBack: () -> NavKey?,
    toLoadingLanguage: () -> Unit
) {
    val viewModel = koinInject<LanguageViewModel>()
    val languageCode by viewModel.languageCode.collectAsState()
    val currentItem = remember(languageCode) { LanguageAvailableApp().fromCode(languageCode) }

    SetupLanguageScreen(
        currentAppLanguageItem = currentItem,
        onConfirmLanguage = { item ->
            viewModel.switchLanguage(item.code.iso)
            toLoadingLanguage()
        },
        exit = { toBack() }
    )
}
