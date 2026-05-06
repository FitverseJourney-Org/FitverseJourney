package com.example.domain.models.local

import androidx.compose.ui.graphics.Color
import com.example.domain.expect.AgeCalculator

data class User(
    val uid: String = "",
    val name: String,
    val email: String,
    val isPremium: Boolean = false,
    val lastname: String,
    val username: String,
    val birthDate: String,
    val weight: Double,
    val height: Int,
    val gender: Genero,
    val classType: ClassType,
    val goals: String,
    val experienceLevel: String,
    val targetWeight: Double? = null,
    val targetCalories: Int? = null,
    val targetProtein: Double?= null,
    val targetCarbs: Double? = null,
    val targetFat: Double? = null,
    val createdAt: Long,
    val updatedAt: Long,
){
    val age: Int
        get() = AgeCalculator.fromBirthDate(birthDate) // "25/04/1998" → 28
}

enum class ClassType(
    val displayName: String,
    val subtitle: String,
    val iconEmoji: String,
    val quote: String,
    val forca: Int,
    val resistencia: Int,
    val nutricao: Int,
    val bonuses: List<String>,
    val accentColor: Color,
    val frameLabel: String,
) {
    TITA(
        displayName = "TITÃ",
        subtitle = "GUERREIRO DA FORÇA",
        iconEmoji = "⚔️",
        quote = "\"Nascido para levantar. Cada kg é um passo rumo à sua forma definitiva.\"",
        forca = 5,
        resistencia = 3,
        nutricao = 2,
        bonuses = listOf(
            "+20% XP em treinos de musculação e força",
            "Vida máxima +20% desde o nível 1",
            "Frame de perfil exclusivo \"Aço Vivo\""
        ),
        accentColor = Color(0xFFFF4500),
        frameLabel = "Aço Vivo",
    ),
    SABIO(
        displayName = "SÁBIO",
        subtitle = "MAGO DA NUTRIÇÃO",
        iconEmoji = "🔮",
        quote = "\"O corpo é resultado da mente. Quem entende o que come vence antes dos outros.\"",
        forca = 2,
        resistencia = 3,
        nutricao = 5,
        bonuses = listOf(
            "+20% XP em missões de nutrição e refeições",
            "Wiki Fitness desbloqueada desde o início",
            "Frame de perfil exclusivo \"Nexo do Saber\""
        ),
        accentColor = Color(0xFF7B5CF5),
        frameLabel = "Nexo do Saber",
    ),
    NOMADE(
        displayName = "NÔMADE",
        subtitle = "ARQUEIRO DA CONSISTÊNCIA",
        iconEmoji = "🏹",
        quote = "\"Não é o mais forte nem o mais sábio — é quem nunca para. A consistência vence todo talento.\"",
        forca = 3,
        resistencia = 5,
        nutricao = 3,
        bonuses = listOf(
            "+20% XP em passos, cardio e hidratação",
            "1 \"escudo de streak\" por semana",
            "Frame de perfil exclusivo \"Horizonte Livre\""
        ),
        accentColor = Color(0xFF22C55E),
        frameLabel = "Horizonte Livre",
    );
}

enum class Objetivo {
    PERDER_PESO,
    GANHAR_MASSA,
    MELHORAR_CONDICAO,
    VIDA_SAUDAVEL,
    PERFORMANCE;
}

enum class NivelExperiencia {
    INICIANTE,
    INTERMEDIARIO,
    AVANCADO;
}

enum class Genero {
    MASCULINO,
    FEMININO;
}

