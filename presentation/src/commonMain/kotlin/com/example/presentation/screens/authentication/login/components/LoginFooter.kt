package com.example.presentation.screens.authentication.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fitversejorneyapp.presentation.generated.resources.Res
import fitversejorneyapp.presentation.generated.resources.login_forget_password
import fitversejorneyapp.presentation.generated.resources.login_not_account
import fitversejorneyapp.presentation.generated.resources.login_sign_up
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginFooter(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row {
            Text(
                text = stringResource(Res.string.login_not_account),
                color = Color.Gray
            )
            Spacer(Modifier.width(4.dp))
            Text(
                stringResource(Res.string.login_sign_up),
                modifier = Modifier.clickable { onNavigateToRegister() },
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = stringResource(Res.string.login_forget_password),
            modifier = Modifier.clickable { onNavigateToForgotPassword() },
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}