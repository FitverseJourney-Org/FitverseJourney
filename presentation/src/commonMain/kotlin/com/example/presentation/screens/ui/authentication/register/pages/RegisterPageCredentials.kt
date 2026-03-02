package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.usecase.authentication.ValidationRegisterScreen.hasMinimumLength
import com.example.domain.usecase.authentication.ValidationRegisterScreen.hasNumber
import com.example.presentation.screens.ui.authentication.login.components.TextFieldAuthentication
import com.example.presentation.states.authentication.RegisterState

@Composable
fun RegisterPageCredentials(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // 🔐 Título
            Text(
                text = "Almost there!",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center

            )

            Spacer(modifier = Modifier.height(6.dp))

            // 🧠 Mensagem explicativa
            Text(
                text = "To complete your registration, please enter your email and create a password. This will keep your progress safe and accessible on any device.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 📧 Email
            TextFieldAuthentication(
                value = { state.email },
                onValueChange = {
                    onAction(RegisterAction.EmailChanged(it))
                },
                txtHint = "Email",
                txtPlaceholder = "example@example.com",
                errorsList = state.emailErrors,
                icon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔑 Password
            TextFieldAuthentication(
                value = { state.password },
                onValueChange = {
                    onAction(RegisterAction.PasswordChanged(it))
                },
                txtHint = "Password",
                txtPlaceholder = "********",
                errorsList = state.passwordErrors,
                hasSupportText = false,
                icon = Icons.Default.Lock
            )
            PasswordStrengthIndicator(
                password = state.password,
                passwordErrors = state.passwordErrors
            )
        }
        Text(
            text = "Your data is securely stored and never shared.",
            color = Color(0xFF81C784),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
fun PasswordStrengthIndicator(
    password: String,
    passwordErrors: List<String>,
    modifier: Modifier = Modifier
) {

    val hasLength = hasMinimumLength(password, 6)
    val hasNumber = hasNumber(password)


    val strength = listOf(hasLength, hasNumber).count { it }

    val progress by animateFloatAsState(
        targetValue = strength / 2f,
        label = "passwordProgress"
    )

    val progressColor = when (strength) {
        0 -> Color.Red
        1 -> Color(0xFFFFA000) // laranja
        else -> Color(0xFF4CAF50) // verde
    }

    Column(modifier = modifier.fillMaxWidth()) {

        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)).background(Color.DarkGray)){
            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progress).background(progressColor))
        }

        Spacer(Modifier.height(12.dp))

        // ✅ Regras
        PasswordRule(
            text = "At least 6 characters",
            isValid = hasLength
        )

        PasswordRule(
            text = "At least 1 number",
            isValid = hasNumber
        )
    }
}

@Composable
fun PasswordRule(
    text: String,
    isValid: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color(0xFF4CAF50) else Color.Red,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            color = if (isValid) Color(0xFF4CAF50) else Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
