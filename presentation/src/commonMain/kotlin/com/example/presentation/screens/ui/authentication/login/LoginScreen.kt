package com.example.presentation.screens.ui.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.auth.login.LoginState
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseButton
import com.example.presentation.screens.widgets.FitverseDivider
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.bg_girl_pose
import org.jetbrains.compose.resources.painterResource
import ui.components.ShapeButton
import ui.theme.FitverseColors

// ─── Entry point ─────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    state: LoginState,
    snackBarHostState: SnackbarHostState,
    onLogin: (email: String, password: String) -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onSocialLogin: (provider: String) -> Unit,
    onOpenLanguage: () -> Unit,
) {
    // Estado de formulário fica aqui — LoginScreenContent é puramente "burro"
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LoginScreenContent(
        snackBarHostState = snackBarHostState,
        emailValue        = email,
        passwordValue     = password,
        onEmailChange     = { email = it },
        onPasswordChange  = { password = it },
        onLogin           = { onLogin(email, password) },
        onForgotPassword  = onForgotPassword,
        onCreateAccount   = onCreateAccount,
        onSocialLogin     = onSocialLogin,
        onOpenLanguage    = onOpenLanguage,
        passwordVisualTransformation  = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

// ─── Content (stateless / testável) ──────────────────────────────────────────

@Composable
fun LoginScreenContent(
    snackBarHostState: SnackbarHostState,
    emailValue: String,
    passwordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onSocialLogin: (provider: String) -> Unit,
    onOpenLanguage: () -> Unit,
    passwordVisualTransformation: VisualTransformation,
) {
    Scaffold(
        containerColor = FitverseColors.Bg,
        snackbarHost   = { SnackbarHost(hostState = snackBarHostState) },
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Hero ──────────────────────────────────────────────────────────
            LoginHero(onOpenLanguage = onOpenLanguage)

            // ── Formulário ───────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = 32.dp),
            ) {
                FitverseOutlinedTextField(
                    value           = emailValue,
                    subtitle        = "E-MAIL",
                    onValueChange   = onEmailChange,
                    placeholder     = "seu@email.com",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )

                FitVerseSpacer(vertical = true, value = 12.dp)

                FitverseOutlinedTextField(
                    value                = passwordValue,
                    subtitle             = "SENHA",
                    onValueChange        = onPasswordChange,
                    placeholder          = "••••••••",
                    visualTransformation = passwordVisualTransformation,
                    trailingIcon = {
                        if(passwordVisualTransformation == VisualTransformation.None){
                            Icon(imageVector = Icons.Default.Visibility, contentDescription = "")
                        }else {
                            Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "")
                        }
                    },
                    keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                FitVerseSpacer(vertical = true, value = 8.dp)

                // Esqueci minha senha
                Box(
                    modifier        = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Text(
                        text       = "Esqueci minha senha",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = FitverseColors.Accent,
                        modifier   = Modifier.clickable(onClick = onForgotPassword),
                    )
                }

                FitVerseSpacer(vertical = true, value = 20.dp)

                FitverseButton(
                    text    = "Entrar",
                    onClick = onLogin,
                    enabled = emailValue.isNotBlank() && passwordValue.isNotBlank(),
                )

                FitVerseSpacer(vertical = true, value = 20.dp)
                FitverseDivider(label = "ou continue com")
                FitVerseSpacer(vertical = true, value = 14.dp)

                SocialLoginRow(onSocialLogin = onSocialLogin)

                FitVerseSpacer(vertical = true, value = 22.dp)

                // Criar conta
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text("Não tem conta ? ", fontSize = 14.sp, color = FitverseColors.TextMuted)
                    Text(
                        text       = "Criar conta",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = FitverseColors.Accent,
                        modifier   = Modifier.clickable(onClick = onCreateAccount),
                    )
                }
            }
        }
    }
}

// ─── Hero ─────────────────────────────────────────────────────────────────────

@Composable
private fun LoginHero(onOpenLanguage: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(FitverseColors.Surface2),
    ) {
        // Imagem de fundo
        Image(
            modifier           = Modifier.fillMaxWidth(),
            painter            = painterResource(Res.drawable.bg_girl_pose),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
        )

        // Gradiente de legibilidade
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FitverseColors.Bg.copy(alpha = 0.85f),
                            FitverseColors.Bg.copy(alpha = 0.85f),
                            FitverseColors.Bg.copy(alpha = 0.85f),
                            FitverseColors.Bg.copy(alpha = 0.85f),
                            FitverseColors.Bg.copy(alpha = 0.95f),
                            FitverseColors.Bg.copy(alpha = 1.00f),
                        ),
                    )
                ),
        )

        // ── Botão de idioma — canto superior direito dentro do Hero ──────────
        IconButton(
            onClick  = onOpenLanguage,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 8.dp, top = 4.dp)
                .size(40.dp)
                .background(
                    color = FitverseColors.Surface.copy(alpha = 0.55f),
                    shape = androidx.compose.foundation.shape.CircleShape,
                ),
        ) {
            Icon(
                imageVector        = Icons.Outlined.Language,
                contentDescription = "Selecionar idioma",
                tint               = FitverseColors.TextPrimary,
                modifier           = Modifier.size(22.dp),
            )
        }

        // Texto de boas-vindas — canto inferior esquerdo
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 16.dp),
        ) {
            Text(
                text       = "BEM-VINDO",
                fontSize   = 36.sp,
                fontWeight = FontWeight.Black,
                color      = FitverseColors.TextPrimary,
                lineHeight = 36.sp,
            )
            Text(
                text          = "DE VOLTA, GUERREIRO",
                fontSize      = 16.sp,
                fontWeight    = FontWeight.ExtraBold,
                color         = FitverseColors.Accent,
                letterSpacing = 0.5.sp,
            )
        }
    }
}

// ─── Social login ─────────────────────────────────────────────────────────────

@Composable
private fun SocialLoginRow(onSocialLogin: (String) -> Unit) {
    val providers = listOf("G" to "Google", "A" to "Apple", "F" to "Facebook")
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        providers.forEach { (label, provider) ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, FitverseColors.Border, ShapeButton)
                    .clip(ShapeButton)
                    .background(FitverseColors.Surface)
                    .clickable { onSocialLogin(provider) }
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = label,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = FitverseColors.TextPrimary,
                )
            }
        }
    }
}