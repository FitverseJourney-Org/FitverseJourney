package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.presentation.theme.FitVerseJourneyTheme
import org.fitverse.project.navigation.FitverseRootNavigation


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

