package com.example.presentation.ui.authentication.register.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.domain.models.user.ClassType
import com.example.domain.models.user.ClassType.*
import com.example.domain.models.user.Genero
import com.example.domain.models.user.NivelExperiencia
import com.example.domain.models.user.Objetivo
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.class_nomade_bonus_1
import fitversejourneyapp.presentation.generated.resources.class_nomade_bonus_2
import fitversejourneyapp.presentation.generated.resources.class_nomade_bonus_3
import fitversejourneyapp.presentation.generated.resources.class_nomade_display_name
import fitversejourneyapp.presentation.generated.resources.class_nomade_frame_label
import fitversejourneyapp.presentation.generated.resources.class_nomade_quote
import fitversejourneyapp.presentation.generated.resources.class_nomade_subtitle
import fitversejourneyapp.presentation.generated.resources.class_sabio_bonus_1
import fitversejourneyapp.presentation.generated.resources.class_sabio_bonus_2
import fitversejourneyapp.presentation.generated.resources.class_sabio_bonus_3
import fitversejourneyapp.presentation.generated.resources.class_sabio_display_name
import fitversejourneyapp.presentation.generated.resources.class_sabio_frame_label
import fitversejourneyapp.presentation.generated.resources.class_sabio_quote
import fitversejourneyapp.presentation.generated.resources.class_sabio_subtitle
import fitversejourneyapp.presentation.generated.resources.class_tita_bonus_1
import fitversejourneyapp.presentation.generated.resources.class_tita_bonus_2
import fitversejourneyapp.presentation.generated.resources.class_tita_bonus_3
import fitversejourneyapp.presentation.generated.resources.class_tita_display_name
import fitversejourneyapp.presentation.generated.resources.class_tita_frame_label
import fitversejourneyapp.presentation.generated.resources.class_tita_quote
import fitversejourneyapp.presentation.generated.resources.class_tita_subtitle
import fitversejourneyapp.presentation.generated.resources.genero_feminino
import fitversejourneyapp.presentation.generated.resources.genero_masculino
import fitversejourneyapp.presentation.generated.resources.nivel_avancado
import fitversejourneyapp.presentation.generated.resources.nivel_iniciante
import fitversejourneyapp.presentation.generated.resources.nivel_intermediario
import fitversejourneyapp.presentation.generated.resources.objetivo_ganhar_massa
import fitversejourneyapp.presentation.generated.resources.objetivo_melhorar_condicao
import fitversejourneyapp.presentation.generated.resources.objetivo_perder_peso
import fitversejourneyapp.presentation.generated.resources.objetivo_performance
import fitversejourneyapp.presentation.generated.resources.objetivo_vida_saudavel
import org.jetbrains.compose.resources.stringResource

@Composable
fun Genero.label(): String = when (this) {
    Genero.MASCULINO -> stringResource(Res.string.genero_masculino)
    Genero.FEMININO  -> stringResource(Res.string.genero_feminino)
}

@Composable
fun NivelExperiencia.label(): String = when (this) {
    NivelExperiencia.INICIANTE     -> stringResource(Res.string.nivel_iniciante)
    NivelExperiencia.INTERMEDIARIO -> stringResource(Res.string.nivel_intermediario)
    NivelExperiencia.AVANCADO      -> stringResource(Res.string.nivel_avancado)
}

@Composable
fun Objetivo.label(): String = when (this) {
    Objetivo.PERDER_PESO       -> stringResource(Res.string.objetivo_perder_peso)
    Objetivo.GANHAR_MASSA      -> stringResource(Res.string.objetivo_ganhar_massa)
    Objetivo.MELHORAR_CONDICAO -> stringResource(Res.string.objetivo_melhorar_condicao)
    Objetivo.VIDA_SAUDAVEL     -> stringResource(Res.string.objetivo_vida_saudavel)
    Objetivo.PERFORMANCE       -> stringResource(Res.string.objetivo_performance)
}

@Composable
fun ClassType.frameLabel(): String = when (this) {
    TITAN -> stringResource(Res.string.class_tita_frame_label)
    SAGE -> stringResource(Res.string.class_sabio_frame_label)  // era SABIO
    NOMAD -> stringResource(Res.string.class_nomade_frame_label) // era NOMADE
}

@Composable
fun ClassType.bonuses(): List<String> = when (this) {
    TITAN -> listOf(
        stringResource(Res.string.class_tita_bonus_1),
        stringResource(Res.string.class_tita_bonus_2),
        stringResource(Res.string.class_tita_bonus_3),
    )
    SAGE -> listOf(
        stringResource(Res.string.class_sabio_bonus_1),
        stringResource(Res.string.class_sabio_bonus_2),
        stringResource(Res.string.class_sabio_bonus_3),
    )
    NOMAD -> listOf(
        stringResource(Res.string.class_nomade_bonus_1),
        stringResource(Res.string.class_nomade_bonus_2),
        stringResource(Res.string.class_nomade_bonus_3),
    )

}

@Composable
fun ClassType.quote(): String = when (this) {
    ClassType.TITAN   -> stringResource(Res.string.class_tita_quote)
    ClassType.SAGE  -> stringResource(Res.string.class_sabio_quote)
    ClassType.NOMAD -> stringResource(Res.string.class_nomade_quote)
}

@Composable
fun ClassType.displayName(): String = when (this) {
    ClassType.TITAN   -> stringResource(Res.string.class_tita_display_name)
    ClassType.SAGE  -> stringResource(Res.string.class_sabio_display_name)
    ClassType.NOMAD -> stringResource(Res.string.class_nomade_display_name)
}

@Composable
fun ClassType.subtitle(): String = when (this) {
    ClassType.TITAN   -> stringResource(Res.string.class_tita_subtitle)
    ClassType.SAGE  -> stringResource(Res.string.class_sabio_subtitle)
    ClassType.NOMAD -> stringResource(Res.string.class_nomade_subtitle)
}

fun ClassType.accentColor(): Color = when (this) {
    ClassType.TITAN -> Color(0xFFFF4500)
    ClassType.SAGE  -> Color(0xFF7B5CF5)
    ClassType.NOMAD -> Color(0xFF22C55E)
}

fun ClassType.iconEmoji(): String = when (this) {
    ClassType.TITAN -> "⚔️"
    ClassType.SAGE  -> "🔮"
    ClassType.NOMAD -> "🏹"
}

fun ClassType.forca(): Int = when (this) {
    ClassType.TITAN -> 5
    ClassType.SAGE  -> 2
    ClassType.NOMAD -> 3
}

fun ClassType.resistencia(): Int = when (this) {
    ClassType.TITAN -> 3
    ClassType.SAGE  -> 3
    ClassType.NOMAD -> 5
}

fun ClassType.nutricao(): Int = when (this) {
    ClassType.TITAN -> 2
    ClassType.SAGE  -> 5
    ClassType.NOMAD -> 3
}
