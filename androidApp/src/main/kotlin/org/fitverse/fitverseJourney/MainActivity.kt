package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.DatabaseDriverFactory
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
        val context = LocalContext.current

        FitverseRootNavigation(
            dbDriverFactory = DatabaseDriverFactory(context)
        )


//        CompositionLocalProvider(
//            LocalAppLocale provides language.iso
//        ) {
//
//        }
//        SetupNavigation()
        //FitVerseNavRoot()
    }
}

