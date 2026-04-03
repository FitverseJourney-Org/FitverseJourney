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
import androidx.datastore.core.DataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expect.AppDataStoreDb
import com.example.expect.DatabaseDriverFactory
import com.example.expect.LocalAppLocale
import com.example.expect.getDefaultLocale
import com.example.presentation.screens.ui.LanguageViewModel
import com.example.presentation.theme.FitVerseJourneyTheme
import org.fitverse.project.navigation.FitverseRootNavigation
import org.koin.compose.koinInject
import java.util.prefs.Preferences


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App(
                dbHelper = DatabaseDriverFactory(this),
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun App(
    dbHelper: DatabaseDriverFactory,
) {
    FitVerseJourneyTheme {

        val viewModel = koinInject<LanguageViewModel>()

        val currentLanguage by viewModel.languageCode.collectAsState()

        LaunchedEffect(Unit){
            viewModel.switchLanguage(currentLanguage)
        }

        CompositionLocalProvider(
            LocalAppLocale provides currentLanguage
        ) {
            FitverseRootNavigation(
                dbHelper = dbHelper
            )
        }
    }
}