package org.fitverse.project.destinations.onboading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.presentation.navigationState.OnboardingNavigation
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.screens.ui.onboarding.state.OnboardingState
import kotlinx.coroutines.flow.collectLatest
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground
import org.koin.compose.koinInject

@Composable
fun OnboardingDestination(
    state : OnboardingState,
    viewmodel: OnboardingViewModel,
    toTrial: () -> Unit,
    toNewAccount: () -> Unit,
    toLogin: () -> Unit

) {

    LaunchedEffect(true){
        viewmodel.navigationState.collectLatest {
            when(it) {
                is OnboardingNavigation.ToTrial -> {
                    toTrial()
                }
                is OnboardingNavigation.ToNewAccount -> {
                    toNewAccount()
                }
                is OnboardingNavigation.ToLogin -> {
                    toLogin()
                }
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()){
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
            }
        )
    }
}


