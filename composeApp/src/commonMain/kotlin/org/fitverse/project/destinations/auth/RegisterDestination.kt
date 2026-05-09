package org.fitverse.project.destinations.auth

import androidx.compose.runtime.Composable
import com.example.presentation.ui.authentication.register.RegisterScreen
import com.example.presentation.ui.authentication.register.RegisterViewModel
import org.koin.compose.koinInject

@Composable
fun RegisterDestination(onBack: () -> Unit) {
    val viewmodel = koinInject<RegisterViewModel>()

    RegisterScreen(
        onBack = {
            onBack()
        },
        viewModel = viewmodel
    )
}