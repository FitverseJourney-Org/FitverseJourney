package com.example.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GamifiedDarkColorScheme: ColorScheme = darkColorScheme(
    primary = DarkGamifiedColors.Primary,
    onPrimary = DarkGamifiedColors.Title,
    primaryContainer = DarkGamifiedColors.PrimarySoft,
    onPrimaryContainer = DarkGamifiedColors.Title,

    secondary = DarkGamifiedColors.Accent,
    onSecondary = DarkGamifiedColors.Title,

    background = DarkGamifiedColors.Background,
    onBackground = DarkGamifiedColors.Body,

    surface = DarkGamifiedColors.Surface,
    onSurface = DarkGamifiedColors.Body,
    surfaceVariant = DarkGamifiedColors.Card,

    error = DarkGamifiedColors.Health,
    onError = DarkGamifiedColors.Title,

    outline = DarkGamifiedColors.Outline
)

private val AppTypography = Typography(
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = FontFamily.Default),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    labelLarge = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
)



//@Composable
//fun FitverseTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//    val colorScheme = darkColorScheme(
//        primary = DarkGamifiedColors.Primary,
//        secondary = DarkGamifiedColors.Accent,
//        background = DarkGamifiedColors.Background,
//        surface = DarkGamifiedColors.Surface,
//        onPrimary = DarkGamifiedColors.Background,
//        onBackground = DarkGamifiedColors.Title,
//        onSurface = DarkGamifiedColors.Body,
//        outline = DarkGamifiedColors.Outline
//    )
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = AppTypography, // Suas fontes personalizadas
//        content = content
//    )
//}
object FitverseColors {
    // Backgrounds (Suaves, não pretos puros)
    val Background = Color(0xFF121214)    // Cinza grafite profundo (confortável)
    val Surface = Color(0xFF1E1E21)       // Superfície dos Cards
    val SurfaceElevated = Color(0xFF2A2A2E) // Modais e Dropdowns

    // Contraste Chamativo (Os detalhes que "saltam")
    val Primary = Color(0xFFA78BFA)       // Roxo Neon
    val Secondary = Color(0xFF22D3EE)      // Ciano (Passos/Progresso)
    val Accent = Color(0xFFFACC15)         // Amarelo XP (Conquistas)

    // Status e Feedback
    val Success = Color(0xFF34D399)
    val Error = Color(0xFFF87171)

    // Tipografia
    val TextPrimary = Color(0xFFF1F1F1)    // Off-white para não ofuscar
    val TextSecondary = Color(0xFFA1A1AA)  // Cinza para descrições
    val Outline = Color(0xFF323238)        // Bordas sutis
}
object BeastModeColors {
    // Backgrounds (Foco Total)
    val Background = Color(0xFF0A0A0A) // Quase preto absoluto
    val Surface = Color(0xFF161616)    // Cards
    val Outline = Color(0xFF262626)    // Bordas sutis

    // Neon de Alta Intensidade
    val Primary = Color(0xFFEAFF00)    // Amarelo Volt (Ação principal)
    val Secondary = Color(0xFFFF4D00)  // Laranja Queimado (Recordes/Alertas)

    // Status
    val Success = Color(0xFF00FF41)    // Verde Matrix
    val XP = Color(0xFFFFFFFF)         // Branco puro para badges

    // Tipografia
    val TextPrimary = Color(0xFFF5F5F5)
    val TextSecondary = Color(0xFF737373)

    // Gradient para o Gráfico de Carga
    val ChartGradient = Brush.verticalGradient(
        colors = listOf(Primary.copy(alpha = 0.4f), Color.Transparent)
    )
}
object CyberpunkColors {
    // Backgrounds (Profundidade Espacial)
    val Background = Color(0xFF0D0B14) // Roxo extremamente escuro
    val Surface = Color(0xFF1A1625)    // Camada de vidro/card
    val Outline = Color(0xFF2D263D)    // Borda roxa escura

    // Neon Elétrico
    val Primary = Color(0xFFBD00FF)    // Roxo Elétrico (Força/Tensão)
    val Secondary = Color(0xFF00F0FF)  // Ciano Neon (Passos/Flow)
    val Accent = Color(0xFF7000FF)     // Violeta Profundo (Detalhes)

    // Status
    val Success = Secondary
    val XP = Color(0xFFFACC15)         // Amarelo Cyber (Contraste alto)

    // Tipografia
    val TextPrimary = Color(0xFFE2E8F0)
    val TextSecondary = Color(0xFF94A3B8)

    // Gradient para o Gráfico de Progressão
    val ChartGradient = Brush.verticalGradient(
        colors = listOf(Secondary.copy(alpha = 0.3f), Color.Transparent)
    )
}
object FitverseTheme {
    // 🌌 BACKGROUNDS (A Base da Imagem_0.png)
    val Background = Color(0xFF242834)    // Cinza Escuro Profundo (Exatamente da imagem)
    val Surface = Color(0xFF2A2E3C)       // Superfície levemente elevada para Cards
    val SurfaceElevated = Color(0xFF323646) // Dropdowns e Modais
    val Outline = Color(0xFF3F4458)       // Divisores e bordas sutis

    // ⚡ ACENTOS NEON (As Estrelas da Imagem_0.png)
    val Primary = Color(0xFFB6FF00)       // Amarelo Neon Volt (Vibrante da imagem)
    val Secondary = Color(0xFF7D53FF)      // Roxo Elétrico (Da imagem)
    val Strength = Secondary              // Roxo Elétrico (Foco em Força/Carga)
    val Cardio = Primary                  // Amarelo Volt (Foco em Passos/Fôlego)

    // Status e Feedback
    val Success = Color(0xFF22C55E)       // Verde Esmeralda (Metas concluídas)
    val Danger = Color(0xFFF87171)        // Rosa/Vermelho (Zonas críticas)
    val XP = Color(0xFFFACC15)            // Dourado (Gamificação)

    // ⚪ TIPOGRAFIA
    val TextPrimary = Color(0xFFFFFFFF)    // Branco Puro (Máximo contraste da imagem)
    val TextSecondary = Color(0xFFD1D5DB)  // Off-white para descrições
    val TextMuted = Color(0xFF6B7280)      // Desabilitados/Dicas

    // 🌈 GRADIENTS (A fluidez do app)
    val PrimaryGradient = Brush.horizontalGradient(
        colors = listOf(Primary, Secondary)
    )
    val ProgressFillGradient = Brush.verticalGradient(
        colors = listOf(Cardio.copy(alpha = 0.3f), Color.Transparent)
    )
}

@Composable
fun FitVerseJourneyTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = FitverseTheme.Primary,
        onPrimary = Color.Black,
        secondary = FitverseTheme.Secondary,
        onSecondary = Color.White,
        tertiary = FitverseTheme.XP,
        background = FitverseTheme.Background,
        surface = FitverseTheme.Surface,
        onBackground = FitverseTheme.TextPrimary,
        onSurface = FitverseTheme.TextSecondary,
        surfaceVariant = FitverseTheme.SurfaceElevated,
        outline = FitverseTheme.Outline
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}


