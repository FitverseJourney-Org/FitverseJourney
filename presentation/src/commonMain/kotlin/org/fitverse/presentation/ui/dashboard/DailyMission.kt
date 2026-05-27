package org.fitverse.presentation.ui.dashboard

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
import org.fitverse.presentation.theme.FitColors

enum class MissionType(val icon: ImageVector, val color: Color) {
    HYDRATION(Icons.Rounded.WaterDrop,      FitColors.Teal),
    CARDIO(Icons.Rounded.DirectionsRun,     FitColors.Orange),
    STRENGTH(Icons.Rounded.FitnessCenter,   FitColors.Accent),
    NUTRITION(Icons.Rounded.Restaurant,     FitColors.Green),
    STEPS(Icons.Rounded.DirectionsWalk,     FitColors.Amber),
    CHALLENGE(Icons.Rounded.EmojiEvents,    FitColors.Amber),
}

data class DailyMission(
    val id: String = "",
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
                title = "Refeição Saudável",
                description = "Registre suas 3 refeições do dia",
                xp = 20,
                type = MissionType.NUTRITION
            ),
        )
    }
}
