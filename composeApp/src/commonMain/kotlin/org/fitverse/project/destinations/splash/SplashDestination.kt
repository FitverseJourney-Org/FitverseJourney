package org.fitverse.project.destinations.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.fitverse.presentation.navigationState.SplashNavigation
import org.fitverse.presentation.ui.splash.AppSplashScreen
import org.fitverse.presentation.ui.splash.viewmodel.SplashViewModel

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