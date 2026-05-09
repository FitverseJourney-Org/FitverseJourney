package org.fitverse.project.destinations.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.presentation.navigationState.SplashNavigation
import com.example.presentation.ui.splash.AppSplashScreen
import com.example.presentation.ui.splash.viewmodel.SplashViewModel

@Composable
fun SplashDestination(
    viewmodel: SplashViewModel,
    toLogin: () -> Unit,
    toTrial: () -> Unit,
    toHome: () -> Unit,
    toOnboarding: () -> Unit,
) {

    LaunchedEffect(Unit){
        viewmodel.navigationState.collect { navigation ->
            when(navigation){
                SplashNavigation.ToHome -> {
                    toHome()
                    println("toHome")
                }
                SplashNavigation.ToOnboarding -> {
                    toOnboarding()
                    println("toOnboarding")
                }
                SplashNavigation.ToTrial -> {
                    toTrial()
                    println("toTrial")
                }
                SplashNavigation.ToAuth -> {
                    toLogin()
                    println("toLogin")
                }
            }
        }
    }

    AppSplashScreen(
        onTimeout = {

        }
    )
}