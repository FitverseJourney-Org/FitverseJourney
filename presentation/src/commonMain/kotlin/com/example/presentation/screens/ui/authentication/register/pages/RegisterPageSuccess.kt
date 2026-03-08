package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.presentation.screens.ui.authentication.login.components.AuthDefaultButton
import com.example.presentation.screens.ui.authentication.register.state.RegisterState


@Composable
fun RegisterPageSuccess(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🎉 Ícone de sucesso
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎉",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ Título
            Text(
                text = "Account created successfully!",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 💪 Mensagem motivacional
            Text(
                text = "Welcome, ${state.name.ifBlank { "athlete" }}!\nYour fitness journey starts now.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            AuthDefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Start my journey",
                onClick = {
                    onAction(RegisterAction.Finish)
                },
                enabled = true
            )
        }

        // 🔹 Mensagem FIXA no rodapé
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            text = "You can change your goals and preferences anytime.",
            color = Color(0xFF81C784),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }

}
