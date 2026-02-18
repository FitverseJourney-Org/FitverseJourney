package com.example.presentation.screens.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.dbLocal.language.Language
import com.example.presentation.screens.authentication.login.components.HeaderContainerLoginScreen
import com.example.presentation.screens.authentication.login.components.LoginFooter
import com.example.presentation.screens.authentication.login.components.LoginForm
import com.example.presentation.states.authentication.LoginState
import com.example.presentation.theme.backgroundBrush
import fitversejorneyapp.presentation.generated.resources.Res
import fitversejorneyapp.presentation.generated.resources.locale_ru
import fitversejorneyapp.presentation.generated.resources.login_subtitle
import fitversejorneyapp.presentation.generated.resources.login_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    currentLanguage: Language,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    snackBarHost: @Composable () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    showLanguageScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = snackBarHost,
        topBar = {
            HeaderContainerLoginScreen(
                showLanguageScreen = showLanguageScreen,
                currentLanguage = currentLanguage
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize().background(
                brush = backgroundBrush
            ).padding(it).padding(top = 35.dp), contentAlignment = Alignment.Center){
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            modifier = Modifier.size(80.dp),
                            painter = painterResource(Res.drawable.locale_ru),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(Res.string.login_title),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = stringResource(Res.string.login_subtitle),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Gray
                            )
                        )
                    }

                    LoginForm(
                        state = state,
                        onEmailChange = onEmailChanged,
                        onPasswordChange = onPasswordChanged,
                        onTogglePasswordVisibility = onTogglePasswordVisibility,
                        onLoginClick = onLoginClick
                    )

                    LoginFooter(
                        onNavigateToRegister = onNavigateToRegister,
                        onNavigateToForgotPassword = onNavigateToForgotPassword
                    )
                }
            }
        }
    )
}