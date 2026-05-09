package org.fitverse.project.destinations.onboading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.onboarding.OnboardingScreen
import com.example.presentation.ui.onboarding.state.OnboardingState
import com.example.presentation.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun OnboardingDestination(
    state : OnboardingState,
    viewmodel: OnboardingViewModel,
    toTrial: () -> Unit,
    toNewAccount: () -> Unit,
    toLogin: () -> Unit

) {
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            DarkGamifiedDashboardBackground()
            OnboardingScreen(
                state = state,
                viewmodel = viewmodel,
                emitToTrial = {
                    viewmodel.emitToTrial()
                },
                emitToNewAccount = {
                    viewmodel.emitToNewAccount()
                },
                emitToLogin = {
                    viewmodel.emitToLogin()
                },
                toTrial = toTrial,
                toNewAccount = toNewAccount,
                toLogin = toLogin
            )
        }
    )
}


