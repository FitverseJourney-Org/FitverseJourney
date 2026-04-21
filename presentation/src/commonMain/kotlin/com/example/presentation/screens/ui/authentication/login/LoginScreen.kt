package com.example.presentation.screens.ui.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.local.language.AppLanguageItem
import com.example.presentation.screens.ui.authentication.login.components.HeaderContainerLoginScreen
import com.example.presentation.screens.ui.authentication.login.components.LoginFooter
import com.example.presentation.screens.ui.authentication.login.components.LoginForm
import com.example.presentation.screens.ui.authentication.login.state.LoginState
import com.example.presentation.screens.ui.authentication.login.viewmodel.LoginViewModel
import com.example.presentation.screens.widgets.FitVerseSpacer
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_logo
import fitversejourneyapp.presentation.generated.resources.login_subtitle
import fitversejourneyapp.presentation.generated.resources.login_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    viewmodel: LoginViewModel,
    currentAppLanguageItem: AppLanguageItem,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    snackBarHost: @Composable () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit,
    showLanguageScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = snackBarHost,
        containerColor = Color.Transparent,
        topBar = {
            HeaderContainerLoginScreen(
                showLanguageScreen = showLanguageScreen,
                currentAppLanguageItem = currentAppLanguageItem
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 35.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(Res.drawable.ico_logo),
                        contentDescription = null
                    )
                    FitVerseSpacer(vertical = true, value = 10.dp)
                    Text(
                        text = stringResource(Res.string.login_title),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = colors.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = stringResource(Res.string.login_subtitle),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = colors.onSurfaceVariant
                        )
                    )
                }

                LoginForm(
                    state = state,
                    onEmailChange = onEmailChanged,
                    onPasswordChange = onPasswordChanged,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onLoginClick = onLoginClick,
                    onGoogleSignIn = {
//                        viewmodel.onGoogleSignIn()
                    },
                )

                LoginFooter(
                    onNavigateToRegister = navigateToRegister,
                    onNavigateToForgotPassword = navigateToForgotPassword,
                )
            }
        }
    }
}