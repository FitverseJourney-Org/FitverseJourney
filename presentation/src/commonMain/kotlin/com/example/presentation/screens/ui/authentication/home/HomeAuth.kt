package com.example.presentation.screens.ui.authentication.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitVerseButton
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.bg_girl_pose
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_apple
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_facebook
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_google
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_loginIn
import fitversejourneyapp.presentation.generated.resources.home_auth_btn_signUp
import fitversejourneyapp.presentation.generated.resources.home_auth_title
import fitversejourneyapp.presentation.generated.resources.ico_apple
import fitversejourneyapp.presentation.generated.resources.ico_facebook
import fitversejourneyapp.presentation.generated.resources.ico_google
import fitversejourneyapp.presentation.generated.resources.ico_logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// Definição das imagens (Crie seus próprios recursos)
data class GridImage(val id: DrawableResource)

@Composable
fun AuthBackgroundGrid() {
    // Mantemos o efeito P&B para o visual "Hero"
    val grayScaleMatrix = remember { ColorMatrix().apply { setToSaturation(0f) } }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Imagem Única de Background
        Image(
            painter = painterResource(resource = Res.drawable.bg_girl_pose),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f), // Ocupa 70% da tela para focar no topo
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(grayScaleMatrix)
        )

        // 2. Overlay de Gradiente "Frio" Único
        // Unifiquei os gradientes para evitar sobreposição excessiva de camadas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // Tom azulado sutil no topo
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black // Termina em preto puro para os botões
                        ),
                        startY = 0f
                    )
                )
        )
    }
}

@Composable
fun HomeAuthScreen(
    onSignUp: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onFacebookSignIn: () -> Unit = {},
    onAppleSignIn: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Background simplificado com a imagem única
        AuthBackgroundGrid()

        // Conteúdo Principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Logo/Icon
            Icon(
                painter = painterResource(resource = Res.drawable.ico_logo),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(resource = Res.string.home_auth_title),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 42.sp,
                    textAlign = TextAlign.Center
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            FitVerseButton(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                text = stringResource(resource = Res.string.home_auth_btn_signUp),
                topColor = colors.primary,
                edgeColor = colors.primaryContainer,
                textColor = colors.onPrimary,
                onClick = onSignUp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botões Sociais
            SocialAuthButton(Res.drawable.ico_google, stringResource(resource = Res.string.home_auth_btn_google), onGoogleSignIn)
            Spacer(modifier = Modifier.height(12.dp))
            SocialAuthButton(Res.drawable.ico_facebook, stringResource(resource = Res.string.home_auth_btn_facebook), onFacebookSignIn)
            Spacer(modifier = Modifier.height(12.dp))
            SocialAuthButton(Res.drawable.ico_apple, stringResource(resource = Res.string.home_auth_btn_apple), onAppleSignIn)

            TextButton(
                onClick = onLogin,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(stringResource(resource = Res.string.home_auth_btn_loginIn), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun SocialAuthButton(
    icon: DrawableResource,
    text: String,
    onClick: () -> Unit
) {
    val color = if(text == "Continue with Apple") Color.Gray else Color.Unspecified
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(resource = icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp).align(Alignment.CenterStart),
                tint = color // Mantém as cores originais dos logos
            )
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}