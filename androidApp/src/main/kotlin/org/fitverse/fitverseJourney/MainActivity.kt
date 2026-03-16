package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expect.LocalAppLocale
import com.example.presentation.theme.GamifiedTheme
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
    GamifiedTheme {

        FitverseRootNavigation()
//        CompositionLocalProvider(
//            LocalAppLocale provides language.iso
//        ) {
//
//        }
//        SetupNavigation()
        //FitVerseNavRoot()
    }
}

