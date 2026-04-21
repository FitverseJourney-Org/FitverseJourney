package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.expect.LocalAppLocale
import com.example.local.database.sqldelight.DatabaseFactory
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun App() {
    FitVerseJourneyTheme {
        val viewModel = koinInject<LanguageViewModel>()
        val currentLanguage by viewModel.languageCode.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.switchLanguage(currentLanguage)
        }

        CompositionLocalProvider(
            LocalAppLocale provides currentLanguage
        ) {
            FitverseRootNavigation() // ✅ Sem parâmetros
        }
    }
}