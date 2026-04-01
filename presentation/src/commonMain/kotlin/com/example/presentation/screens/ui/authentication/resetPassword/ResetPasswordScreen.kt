package com.example.presentation.screens.ui.authentication.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField
import com.example.presentation.screens.ui.authentication.resetPassword.components.ResetPasswordTopBar
import com.example.presentation.screens.ui.authentication.resetPassword.state.ResetPasswordState
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.screens.widgets.FitverseTopAppBar
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_reset
import org.jetbrains.compose.resources.painterResource

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    snackBarHostState: @Composable () -> Unit,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    emailErrors: List<String>,
    onSendResetLink: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent, // Fundo amarrado ao tema (geralmente preto ou um azul/cinza muito escuro)
        snackbarHost = snackBarHostState,
        topBar = {
            ResetPasswordTopBar(onBackClick = onBackClick)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(Res.drawable.ico_reset),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Start)
            )


            Text(
                text = "Reset your password",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = colors.onBackground, // Adapta-se automaticamente se você tiver Dark/Light mode
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Enter your email and we’ll send you a secure link to reset your password.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colors.onSurfaceVariant // Tom acinzentado frio, melhor que o Color.Gray direto
                )
            )

            Spacer(Modifier.height(32.dp))

            FitverseOutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = "Email",
                placeholder = "your@email.com",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = colors.primary // Ícone ganha a cor fria principal para dar destaque
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSendResetLink() }
                ),
                isError = emailErrors.isNotEmpty(),
                errorText = emailErrors.firstOrNull()
            )

            Spacer(Modifier.height(24.dp))

            FitVerseButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = "Send reset link",
                topColor = colors.primary, // O botão principal leva a cor fria de destaque
                edgeColor = colors.primary.copy(alpha = 0.8f),
                textColor = colors.onPrimary,
                onClick = onSendResetLink,
                enabled = emailErrors.isEmpty() && state.email.isNotBlank(), // Evita clique com e-mail vazio
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Remembered your password? Sign in",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigateToLogin() }
                    .padding(8.dp), // Melhora a área de toque (touch target)
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colors.primary, // Indica que é um texto clicável (link) usando a cor do app
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}