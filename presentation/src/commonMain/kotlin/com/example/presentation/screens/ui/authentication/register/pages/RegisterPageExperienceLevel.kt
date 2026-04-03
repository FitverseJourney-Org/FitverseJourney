package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.GridOptionCard
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@Composable
fun RegisterPageExperienceLevel(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Qual sua experiência?",
            subtitle = "Isso ajustará a intensidade dos seus treinos iniciais."
        )

        // Definimos cores que remetem à progressão (Cinza -> Verde -> Roxo -> Dourado)
        val levels = listOf(
            ExperienceLevelItem(
                "Sedentário",
                Icons.Default.Cloud,
                Color(0xFF94A3B8).copy(alpha = 0.15f),
                Color(0xFF475569),
                RegisterExperienceLevel.SEDENTARY
            ),
            ExperienceLevelItem(
                "Iniciante",
                Icons.Default.DirectionsRun,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.primary,
                RegisterExperienceLevel.BEGINNER
            ),
            ExperienceLevelItem(
                "Intermediário",
                Icons.Default.FitnessCenter,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.secondary,
                RegisterExperienceLevel.INTERMEDIATE
            ),
            ExperienceLevelItem(
                "Atleta",
                Icons.Default.EmojiEvents,
                Color(0xFFFACC15).copy(alpha = 0.2f),
                Color(0xFFD97706),
                RegisterExperienceLevel.ADVANCED
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentPadding = PaddingValues(10.dp),
        ) {
            items(levels) { item ->
                GridOptionCard(
                    text = item.title,
                    icon = item.icon,
                    iconBgColor = item.bgColor, // Cor sólida/translúcida, nunca branca
                    iconTint = item.tint,
                    isSelected = state.registerExperienceLevel == item.level,
                    onClick = { onAction(RegisterAction.ExperienceLevelChanged(item.level)) }
                )
            }
        }
    }
}

// Helper class para organizar os dados da lista
data class ExperienceLevelItem(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color,
    val tint: Color,
    val level: RegisterExperienceLevel
)
