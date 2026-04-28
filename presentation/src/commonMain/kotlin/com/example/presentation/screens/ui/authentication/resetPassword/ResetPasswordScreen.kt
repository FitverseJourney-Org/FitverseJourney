package com.example.presentation.screens.ui.authentication.resetPassword

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseButton
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.components.FitverseTopBar
import kotlinx.coroutines.delay

@Composable
fun ResetPasswordScreen(
    onBack: () -> Unit,
    onSendLink: (email: String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }

    // Auto-navigate back after 3 seconds when email is sent
    LaunchedEffect(emailSent) {
        if (emailSent) {
            delay(3_000L)
            onBack()
        }
    }

    Scaffold(
        topBar = {
            FitverseTopBar(onBack = onBack)
        },
        containerColor = FitverseColors.Bg,
        content = { paddingValues ->
            AnimatedContent(
                targetState = emailSent,
                transitionSpec = {
                    fadeIn(animationSpec = tween(600)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "ResetPasswordContent"
            ) { sent ->
                if (sent) {
                    // ── Success State ──────────────────────────────────────────
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Green checkmark box
                            Box(
                                modifier = Modifier.size(72.dp)
                                    .background(
                                        color = Color(0xFF2ECC40),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Sucesso",
                                    tint = Color.White,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            FitVerseSpacer(value = 24.dp, vertical = true)

                            Text(
                                text = "E-MAIL ENVIADO!",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF2ECC40),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            )

                            FitVerseSpacer(value = 8.dp, vertical = true)

                            Text(
                                text = "Verifique sua caixa de entrada.",
                                fontSize = 16.sp,
                                color = FitverseColors.TextMuted,
                            )
                        }
                    }
                }
                else {
                    // ── Form State ─────────────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .padding(paddingValues)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            FitVerseSpacer(value = 8.dp, vertical = true)
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "🔑",
                                fontSize = 50.sp
                            )
                            FitVerseSpacer(value = 8.dp, vertical = true)
                            Text(
                                text = "RECUPERAR ACESSO",
                                style = MaterialTheme.typography.labelMedium,
                                color = FitverseColors.TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                            )
                            FitVerseSpacer(value = 8.dp, vertical = true)
                            Text(
                                text = "Insira seu e-mail e enviaremos um link de redefinição de senha.",
                                fontSize = 16.sp,
                                color = FitverseColors.TextMuted,
                                lineHeight = 19.sp,
                            )
                            FitVerseSpacer(value = 28.dp, vertical = true)
                            FitverseOutlinedTextField(
                                value = email,
                                subtitle = "E-MAIL",
                                onValueChange = { email = it },
                                placeholder = "seu@email.com",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            FitVerseSpacer(value = 20.dp, vertical = true)
                            FitverseButton(
                                text = "Enviar Link",
                                onClick = {
                                    onSendLink(email)
                                    emailSent = true   // trigger AnimatedContent
                                },
                                enabled = email.isNotBlank(),
                            )
                        }
                    }
                }
            }
        }
    )
}