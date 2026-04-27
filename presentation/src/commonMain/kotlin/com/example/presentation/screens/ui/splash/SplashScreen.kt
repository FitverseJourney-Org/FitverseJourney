package com.example.presentation.screens.ui.splash

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_logo
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

// Cores Diretas conforme seu Design System (image_4e8704)
private val ColorBackground = Color(0xFF0A0B0F)
private val ColorPrimaryPurple = Color(0xFF7C3AED)
private val ColorTextMuted = Color(0xFF71717A)

@Composable
fun AppSplashScreen(onTimeout: () -> Unit) {
    // 1. Iniciamos com 0f
    var startAnimation by remember { mutableStateOf(false) }

    // 2. O targetValue só muda para 1f quando startAnimation for true
    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000, // 2 segundos exatos
            easing = EaseOut
        ),
        label = "SplashProgress"
    )

    LaunchedEffect(Unit) {
        // Pequeno delay inicial para garantir que o layout foi medido
        delay(100)
        startAnimation = true

        // Aguardamos os 2000ms da animação + um fôlego para o usuário ver a barra cheia
        onTimeout()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground),
        contentAlignment = Alignment.Center
    ) {
        // --- CONTEÚDO CENTRAL ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Glow de fundo para o logo
                Spacer(
                    modifier = Modifier
                        .size(160.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    ColorPrimaryPurple.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Image(
                    painter = painterResource(resource = Res.drawable.ico_logo),
                    contentDescription = "Logo Fitverse",
                    modifier = Modifier.size(240.dp)
                )
            }

            Spacer(Modifier.height(40.dp))

            Text(
                text = "FITVERSE",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                letterSpacing = 8.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "YOUR JOURNEY STARTS NOW",
                color = ColorTextMuted,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.CenterStart
            ) {
                // Indicador de progresso (Roxo Primary #7C3AED)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        // Usamos a variável 'progress' animada aqui
                        .fillMaxWidth(progress)
                        .background(Color(0xFF7C3AED))
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "v1.0.0",
                color = Color(0xFF71717A).copy(alpha = 0.4f), // Text Muted
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}