package com.example.presentation.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.presentation.theme.FitverseColors

enum class MissionType(val icon: ImageVector, val color: Color) {
    STRETCH(Icons.Rounded.SelfImprovement,  FitverseColors.Purple),
    HYDRATION(Icons.Rounded.WaterDrop,      FitverseColors.Teal),
    CARDIO(Icons.Rounded.DirectionsRun,     FitverseColors.Orange),
    STRENGTH(Icons.Rounded.FitnessCenter,   FitverseColors.Accent),
    NUTRITION(Icons.Rounded.Restaurant,     FitverseColors.Green),
    SLEEP(Icons.Rounded.Bedtime,            FitverseColors.Blue),
    STEPS(Icons.Rounded.DirectionsWalk,     FitverseColors.Amber),
    MEDITATION(Icons.Rounded.Spa,           FitverseColors.Purple),
    CHALLENGE(Icons.Rounded.EmojiEvents,    FitverseColors.Amber),
}

data class DailyMission(
    val title: String,
    val description: String,
    val xp: Int?,
    val type: MissionType,
    val isCompleted: Boolean = false
){
    companion object {
        val defaultDailyMissions = listOf(
            DailyMission(
                title = "Desafio do Dia",
                description = "Complete 100 agachamentos hoje",
                xp = 100,
                type = MissionType.CHALLENGE
            ),
            DailyMission(
                title = "Morning Stretch",
                description = "5–10 min • Aqueça seu corpo",
                xp = 10,
                type = MissionType.STRETCH
            ),
            DailyMission(
                title = "Registrar Hidratação",
                description = "8 copos • Meta diária de água",
                xp = 15,
                type = MissionType.HYDRATION,
                isCompleted = true
            ),
            DailyMission(
                title = "Cardio 30 min",
                description = "Caminhar • Correr • Bike",
                xp = 30,
                type = MissionType.CARDIO
            ),
            DailyMission(
                title = "Treino de Força",
                description = "3 séries • Membros superiores",
                xp = 50,
                type = MissionType.STRENGTH
            ),
            DailyMission(
                title = "Refeição Saudável",
                description = "Registre suas 3 refeições do dia",
                xp = 20,
                type = MissionType.NUTRITION
            ),
        ).sortedByDescending { it.type == MissionType.CHALLENGE }
    }
}
