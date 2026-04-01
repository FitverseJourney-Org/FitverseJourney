package org.fitverse.project.destinations.onboading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.presentation.navigationState.OnboardingNavigation
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.states.onboarding.OnboardingState
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun OnboardingDestination(
    state : OnboardingState,
    viewmodel: OnboardingViewModel,
    toTrial: () -> Unit,

) {

    LaunchedEffect(true){
        viewmodel.navigationState.collectLatest {
            when(it) {
                is OnboardingNavigation.ToTrial -> {
                    toTrial()
                }
            }
        }
    }


    OnboardingScreen(
        state = state,
        viewmodel = viewmodel,
        emitToTrial = {
            viewmodel.emitToTrial()
        }
    )
}


