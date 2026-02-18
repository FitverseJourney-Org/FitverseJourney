package org.fitverse.fitverseJourney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expect.LocalAppLocale
import com.example.presentation.presenter.AppPresenter
import org.fitverse.fitverseJourney.navigation.SetupNavigation
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
    MaterialTheme {
        val appPresenter = koinInject<AppPresenter>()
        val language by appPresenter.languageState.collectAsStateWithLifecycle()

        CompositionLocalProvider(
            LocalAppLocale provides language.iso
        ) {
            SetupNavigation()
        }

    }
}