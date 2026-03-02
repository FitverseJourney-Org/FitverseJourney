package org.fitverse.fitverseJourney.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.domain.model.authentication.login.LoginAction
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import com.example.presentation.screens.ui.authentication.resetPassword.ResetPasswordViewModel
import com.example.presentation.screens.ui.onboarding.OnboardingViewModel
import org.fitverse.fitverseJourney.routes.LoadingNewLanguageRoute
import org.fitverse.fitverseJourney.routes.SplashRoute
import org.fitverse.fitverseJourney.routes.authentication.LoginRoute
import org.fitverse.fitverseJourney.routes.authentication.RegisterRoute
import org.fitverse.fitverseJourney.routes.authentication.ResetPasswordRoute
import org.fitverse.fitverseJourney.routes.main.MainScreen
import org.fitverse.fitverseJourney.routes.onboarding.OnboardingRoute
import org.fitverse.fitverseJourney.routes.trial.TrialIntroRoute
import org.koin.compose.koinInject


@Composable
fun SetupNavigation() {
    val navController = rememberNavController()


    Surface {
        NavHost(
            navController = navController,
            startDestination = "splash",
            route = NavGraph.ROOT,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                EnterTransition.None
            },
            popExitTransition = {
                ExitTransition.None
            }
        ) {
            composable("splash") {
                SplashRoute(
                    onFinish = { navController.navigate("onboarding") }
                )
            }
            navigation(startDestination = "onboarding", route = "graph_onboarding") {
                composable("onboarding") {
                    val viewmodel = koinInject<OnboardingViewModel>()
                    val state by viewmodel.state.collectAsState()
                    OnboardingRoute(
                        state = state,
                        onFinish = { navController.navigate("trial/intro") },
                        nextPage = {
                            viewmodel.nextPage()
                        },
                        skipToLastPage = {
                            viewmodel.skipToLastPage()
                        },
                    )
                }
            }
            composable("trial/intro") {
                TrialIntroRoute(
                    onSkip = { navController.navigate("graph_auth") { popUpTo("onboarding") { inclusive = true } } },
                    onStartTrial = { navController.navigate("graph_auth") { popUpTo("trial/intro") { inclusive = true } } }
                )
            }
            navigation(startDestination = "login", route = "graph_auth") {
                composable("login") {
                    val viewmodel = koinInject<LoginViewModel>()
                    val state = viewmodel.state.collectAsState()
                    LoginRoute(
                        presenter = viewmodel,
                        onNavigateToRegister = { navController.navigate("register") },
                        onNavigateToResetPassword = {
                            navController.navigate(Routes.ResetPassword.route)
                        },
                        onConfirmLanguage = { selectedLanguage ->
                            val route = Routes.LoadingNewLanguage.create(
                                selectedLanguage.code.iso
                            )
                            // 🔒 GUARD: evita múltiplas navegações
                            if (navController.currentDestination?.route
                                    ?.startsWith("loadingNewLanguage") == true
                            ) return@LoginRoute

                            viewmodel.onAction(LoginAction.LanguageChanged(selectedLanguage))

                            navController.navigate(route)
                        },
                        onLoginSuccess = {
                            navController.navigate("main") {
                                popUpTo("auth") { inclusive = true }
                            }
                        },
                    )
                }
                composable("register") {
                    val viewmodel = koinInject<RegisterViewModel>()
                    val state = viewmodel.state.collectAsState()

                    RegisterRoute(
                        onExitToLogin = {
                            navController.navigate("auth")
                        },
                        presenter = viewmodel
                    )
                }
                composable("resetPassword") {
                    val viewmodel = koinInject<ResetPasswordViewModel>()
                    ResetPasswordRoute(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        viewmodel = viewmodel
                    )
                }
            }



            // LoadingNewLanguage (com argumento)
//            composable(
//                route = Routes.LoadingNewLanguage.route,
//                arguments = listOf(navArgument("langIso") { type = NavType.StringType })
//            ) { backStackEntry ->
//                LoadingNewLanguageRoute(
//                    navController = navController,
//                    backStackEntry = backStackEntry,
//                    appViewModel = koinInject()
//                )
//            }

            composable("main") {
                MainScreen()
            }
        }

    }
}

sealed class Routes(val route: String) {
    // --- global screens
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")

    // --- auth
    object Login : Routes("login")
    object Register : Routes("register")
    object ResetPassword : Routes("resetPassword")
    object SetupLanguage : Routes("setupLanguage")

    object LoadingNewLanguage : Routes("loadingNewLanguage/{langIso}") {
        fun create(iso: String) = "loadingNewLanguage/$iso"
    }

    // --- trial / misc
    object TrialIntro : Routes("trial/intro")
    object ChoosePackage : Routes("trial/choosePackage")

    // --- main (hierarquia explícita)
    object MainRoot : Routes("main") // opcional: grafo raiz para main
    object Dashboard : Routes("${MainRoot.route}/dashboard")
    object DashboardNotification : Routes("${Dashboard.route}/notification")

    object Workout : Routes("${MainRoot.route}/workout")
    object WorkoutSession : Routes("${Workout.route}/session") // ex: "main/workout/session"

    object Nutrition : Routes("${MainRoot.route}/nutrition")

    object Profile : Routes("${MainRoot.route}/profile") {
        const val ARG_USER = "userId"
        val routeWithArg = "${route}/{$ARG_USER}"
        fun withUser(userId: String) = "${route}/$userId"
    }

    object Plans : Routes("${MainRoot.route}/plans")
}

object NavGraph {
    const val ROOT = "graph_root"
    const val AUTH = "graph_authentication"
    const val ONBOARDING = "graph_onboarding"
    const val MAIN_ROOT = "graph_main_root"
    const val MAIN = "graph_main"
}

object NavGraphMain {
    const val ROOT = "graph_root"
    const val DASHBOARD = "graph_dashboard"
    const val WORKOUT = "graph_workout"
    const val NUTRITION = "graph_nutrition"
    const val PLANS = "graph_plans"
    const val PROFILE = "graph_profile"
}

fun slideInFromLeft(): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(800),
        initialOffsetX = { fullWidth -> -fullWidth }
    )
fun slideOutToLeft(): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(800),
        targetOffsetX = { fullWidth -> -fullWidth }
    )
fun slideInFromRight(): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(800),
        initialOffsetX = { fullWidth -> fullWidth }
    )
fun slideOutToRight(): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(800),
        targetOffsetX = { fullWidth -> fullWidth }
    )


