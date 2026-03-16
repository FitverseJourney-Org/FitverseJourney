package org.fitverse.project.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.presentation.navigationState.OnboardingNavigation
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.screens.ui.onboarding.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun OnboardingDestination(
    toTrial: () -> Unit,
    toLogin: () -> Unit,
) {
    val viewmodel = koinInject<OnboardingViewModel>()
    val state by viewmodel.state.collectAsState()



    LaunchedEffect(true){
        viewmodel.navigationState.collectLatest {
            when(it) {
                is OnboardingNavigation.ToTrial -> {
                    toTrial()
                }
                is OnboardingNavigation.ToLogin -> {
                    toLogin()
                }
            }
        }
    }


    OnboardingScreen(
        state = state,
        onFinish = {
            viewmodel.emitToTrial()
        },
        viewmodel = viewmodel,
    )
}


