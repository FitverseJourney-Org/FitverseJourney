package com.example.presentation.screens.authentication.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import com.example.presentation.screens.authentication.login.components.AuthDefaultButton
import com.example.presentation.screens.authentication.login.components.TextFieldAuthentication
import com.example.presentation.screens.authentication.resetPassword.components.ResetPasswordTopBar
import com.example.presentation.states.authentication.ResetPasswordState
import com.example.presentation.theme.backgroundBrush

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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        snackbarHost = snackBarHostState,
        topBar = {
            ResetPasswordTopBar(onBackClick)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(backgroundBrush)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(32.dp))

//            Image(
//                painter = painterResource(Res.drawable.),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(140.dp)
//                    .align(Alignment.Start)
//            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Reset your password",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Enter your email and we’ll send you a secure link to reset your password.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )

            Spacer(Modifier.height(32.dp))

            TextFieldAuthentication(
                value = { state.email },
                onValueChange = onEmailChange,
                icon = Icons.Outlined.Email,
                txtHint = "Email",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSendResetLink() }
                ),
                txtPlaceholder = "you@email.com",
                errorsList = emptyList()
            )

            Spacer(Modifier.height(24.dp))

            AuthDefaultButton(
                text = "Send reset link",
                isLoading = isLoading,
                enabled = emailErrors.isEmpty(),
                onClick = onSendResetLink
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Remembered your password? Sign in",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigateToLogin() },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}


