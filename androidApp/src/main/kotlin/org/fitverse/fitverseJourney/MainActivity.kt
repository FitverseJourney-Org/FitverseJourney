package org.fitverse.fitverseJourney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.expect.AppEnvironment
import com.example.presentation.screens.ui.LanguageViewModel
import com.example.presentation.theme.FitVerseJourneyTheme
import org.fitverse.project.navigation.FitverseRootNavigation
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    FitVerseJourneyTheme {
        val viewModel       = koinInject<LanguageViewModel>()
        val currentLanguage by viewModel.languageCode.collectAsState()

        // ✅ Passa o idioma explicitamente para AppEnvironment.
        //    AppEnvironment atualiza currentLocale e força recomposição via key().
        AppEnvironment(language = currentLanguage) {
            FitverseRootNavigation()
        }
    }
}
