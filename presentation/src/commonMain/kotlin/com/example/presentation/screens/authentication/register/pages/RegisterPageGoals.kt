package com.example.presentation.screens.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.Goal
import com.example.domain.model.authentication.register.RegisterAction
import com.example.presentation.states.authentication.RegisterState


@Composable
fun RegisterPageGoals(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "What’s your main goal?",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(32.dp))

        FitnessGoalsSelector(
            selectedGoal = state.goals,
            onGoalSelected = {
                onAction(RegisterAction.GoalsChanged(it))
            }
        )
    }
}

data class FitnessGoal(
    val title: String,
    val description: String,
    val iconText: String
)
object FitnessGoals {

    fun all(): List<com.example.presentation.screens.authentication.register.pages.FitnessGoal> = listOf(
        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.FitnessGoal(
            title = "Lose weight",
            description = "Burn fat and reduce body weight",
            iconText = "🔥"
        ),
        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.FitnessGoal(
            title = "Build muscle",
            description = "Increase strength and muscle mass",
            iconText = "💪"
        ),
        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.FitnessGoal(
            title = "Improve endurance",
            description = "Boost stamina and cardiovascular health",
            iconText = "🏃"
        ),
        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.FitnessGoal(
            title = "Stay healthy",
            description = "Maintain a balanced and healthy lifestyle",
            iconText = "❤️"
        )
    )
}
@Composable
fun FitnessGoalsSelector(
    selectedGoal: Goal?,
    onGoalSelected: (Goal) -> Unit,
    modifier: Modifier = Modifier
) {
    val goals = remember { _root_ide_package_.com.example.presentation.screens.authentication.register.pages.FitnessGoals.all() }


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        goals.forEachIndexed { _, goal ->
            val isSelected =
                selectedGoal?.code == goal.title.replace(" ", "_").uppercase()

            _root_ide_package_.com.example.presentation.screens.authentication.register.pages.OptionsRegister(
                title = goal.title,
                description = goal.description,
                iconText = goal.iconText,
                isSelected = isSelected,
                onClick = {
                    onGoalSelected(
                        Goal(
                            code = goal.title.replace(" ", "_").uppercase(),
                            description = goal.description
                        )
                    )
                }
            )
        }

    }
}
@Composable
fun OptionsRegister(
    title: String,
    description: String,
    iconText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            Color(0xFF1E7F1E)
        else
            Color(0xFF0F2F0F),
        label = "background"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Ícone / Emoji
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
            }

            Spacer(Modifier.width(16.dp))

            // Texto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
