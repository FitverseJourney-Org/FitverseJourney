package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.example.expect.AppDataStoreDb
import com.example.expect.DatabaseDriverFactory
import com.example.expect.LocalAppLocale
import com.example.presentation.theme.FitVerseJourneyTheme
import org.fitverse.project.navigation.FitverseRootNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {

            // 1. Inicializa o contexto do seu AppDataStoreDb
            AppDataStoreDb.context = this


            App(dbHelper = DatabaseDriverFactory(this))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun App(
    dbHelper: DatabaseDriverFactory,
) {
    FitVerseJourneyTheme {

        FitverseRootNavigation(
            dbHelper = dbHelper,
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

