package org.fitverse.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ══════════════════════════════════════════════════════════════════════════════
// Fitverse — Design Token de Tipografia
//
// Escala semântica única usada em toda a UI.
// Regras:
//   · Sempre referencie um token — nunca hardcode fontSize/fontWeight direto.
//   · Adicione novos tokens aqui antes de usá-los em telas.
//   · Mapeamento para MaterialTheme.typography está em Theme.kt.
//
// ── Como ativar Bebas Neue (fonte de display) ─────────────────────────────────
//   1. Copie `androidApp/src/main/res/font/bebasneueregular.ttf`
//      para `presentation/src/commonMain/composeResources/font/bebas_neue_regular.ttf`
//   2. No arquivo de tema ou @Composable, carregue:
//          val bebasNeue = FontFamily(Font(Res.font.bebas_neue_regular))
//   3. Substitua `FontFamily.Default` em FVTypography.Display.large / medium.
// ═════════════════════════════════════════════════════════════════════════════

object FVTypography {

    // ── Display ────────────────────────────────────────────────────────────────
    // Títulos de tela inteira: "TREINO DO DIA", "NUTRIÇÃO", "CONQUISTAS"
    // Sempre em uppercase. Candidato a Bebas Neue quando disponível.

    val displayLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 28.sp,
        fontWeight    = FontWeight.Black,
        letterSpacing = (-0.5).sp,
        lineHeight    = 36.sp,
    )

    val displayMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 26.sp,
        fontWeight    = FontWeight.Black,
        letterSpacing = (-0.3).sp,
        lineHeight    = 30.sp,
    )

    // ── Headline ───────────────────────────────────────────────────────────────
    // Títulos de seção, nomes de modal, top bars de sub-tela

    val headlineLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 22.sp,
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 1.sp,
        lineHeight    = 28.sp,
    )

    val headlineMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 18.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight    = 24.sp,
    )

    val headlineSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 16.sp,
        fontWeight    = FontWeight.Black,
        letterSpacing = 1.sp,
        lineHeight    = 22.sp,
    )

    // ── Title ──────────────────────────────────────────────────────────────────
    // Títulos de card, nome de item em lista

    val titleLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 15.sp,
        fontWeight    = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight    = 20.sp,
    )

    val titleMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 13.sp,
        fontWeight    = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight    = 18.sp,
    )

    val titleSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 12.sp,
        fontWeight    = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight    = 16.sp,
    )

    // ── Body ───────────────────────────────────────────────────────────────────
    // Texto corrido, descrições, condições de conquista

    val bodyLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 14.sp,
        fontWeight    = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight    = 22.sp,
    )

    val bodyMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 13.sp,
        fontWeight    = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight    = 18.sp,
    )

    val bodySmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 12.sp,
        fontWeight    = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight    = 16.sp,
    )

    // ── Label ──────────────────────────────────────────────────────────────────
    // Chips, filtros, texto de botão

    val labelLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 12.sp,
        fontWeight    = FontWeight.SemiBold,
        letterSpacing = 0.sp,
        lineHeight    = 16.sp,
    )

    val labelMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 11.sp,
        fontWeight    = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight    = 14.sp,
    )

    val labelSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight    = 12.sp,
    )

    // ── Overline ───────────────────────────────────────────────────────────────
    // Rótulos ALL_CAPS acima de seções e cards: "TREINO DE HOJE", "SEGUNDA-FEIRA"
    // Sempre renderizar em uppercase().

    val overline = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        lineHeight    = 12.sp,
    )

    val overlineLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 11.sp,
        fontWeight    = FontWeight.Medium,
        letterSpacing = 1.5.sp,
        lineHeight    = 14.sp,
    )

    // ── Caption ────────────────────────────────────────────────────────────────
    // Rarity badges, footnotes, texto mínimo de apoio

    val caption = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 9.sp,
        fontWeight    = FontWeight.Normal,
        letterSpacing = 0.sp,
        lineHeight    = 12.sp,
    )

    val captionBold = TextStyle(
        fontFamily    = FontFamily.Default,
        fontSize      = 9.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        lineHeight    = 12.sp,
    )

    // ── Mono ───────────────────────────────────────────────────────────────────
    // Valores numéricos: XP, calorias, estatísticas, contadores

    val monoLarge = TextStyle(
        fontFamily    = FontFamily.Monospace,
        fontSize      = 22.sp,
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = (-0.5).sp,
        lineHeight    = 26.sp,
    )

    val monoMedium = TextStyle(
        fontFamily    = FontFamily.Monospace,
        fontSize      = 14.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 0.sp,
        lineHeight    = 18.sp,
    )

    val monoSmall = TextStyle(
        fontFamily    = FontFamily.Monospace,
        fontSize      = 11.sp,
        fontWeight    = FontWeight.Medium,
        letterSpacing = 0.sp,
        lineHeight    = 14.sp,
    )
}
