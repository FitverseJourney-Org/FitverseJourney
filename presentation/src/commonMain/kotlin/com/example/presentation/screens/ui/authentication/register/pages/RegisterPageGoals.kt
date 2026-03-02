package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.Goal
import com.example.domain.model.authentication.register.RegisterAction
import com.example.presentation.states.authentication.RegisterState

/**
 * Page root: mantém tipografia e cores via MaterialTheme.
 */
@Composable
fun RegisterPageGoals(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What’s your main goal?",
            color = colors.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "We’ll tailor workouts and meals to help you reach it.",
            color = colors.onBackground.copy(alpha = 0.78f),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(28.dp))

        FitnessGoalsSelector(
            selectedGoal = state.goals,
            onGoalSelected = {
                onAction(RegisterAction.GoalsChanged(it))
            }
        )
    }
}

/* ---------------- Models + static list ---------------- */

private data class FitnessGoal(
    val code: String,
    val title: String,
    val description: String
)

private fun defaultGoals(): List<FitnessGoal> = listOf(
    FitnessGoal(code = "LOSE_WEIGHT", title = "Lose weight", description = "Burn fat and reduce body weight"),
    FitnessGoal(code = "BUILD_MUSCLE", title = "Build muscle", description = "Increase strength and muscle mass"),
    FitnessGoal(code = "IMPROVE_ENDURANCE", title = "Improve endurance", description = "Boost stamina and cardiovascular health"),
    FitnessGoal(code = "STAY_HEALTHY", title = "Stay healthy", description = "Maintain a balanced and healthy lifestyle")
)

/* ---------------- Selector ---------------- */

@Composable
fun FitnessGoalsSelector(
    selectedGoal: Goal?,
    onGoalSelected: (Goal) -> Unit,
    modifier: Modifier = Modifier
) {
    val goals = remember { defaultGoals() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        goals.forEach { goal ->
            val isSelected = selectedGoal?.code == goal.code
            GoalOptionCard(
                title = goal.title,
                description = goal.description,
                isSelected = isSelected,
                onClick = {
                    onGoalSelected(Goal(code = goal.code, description = goal.description))
                }
            )
        }
    }
}

/* ---------------- Option Card (improved) ---------------- */

@Composable
fun GoalOptionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    // background changes slightly when selected
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colors.primaryContainer.copy(alpha = 0.12f) else colors.surface
    )

    // animated accent/border
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.outline.copy(alpha = 0.55f)
    )

    val scale by animateFloatAsState(targetValue = if (isSelected) 1.02f else 1f, animationSpec = spring())

    // choose icon + tint according to title (semantic)
    val (icon, iconAccent) = goalToIcon(title, colors)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isSelected) 8.dp else 2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .then(Modifier),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // icon circle with subtle gradient
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(iconAccent.copy(alpha = 0.22f), iconAccent.copy(alpha = 0.08f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title icon",
                    tint = iconAccent,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = description,
                    color = colors.onSurface.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.width(12.dp))

            // selection indicator (check icon) + border hint
            if (isSelected) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = iconAccent.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, iconAccent.copy(alpha = 0.18f))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "selected",
                            tint = iconAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Selected",
                            color = iconAccent,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                // small outline hint when not selected
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(borderColor)
                )
            }
        }
    }
}

/* ---------------- icon mapping ---------------- */

private fun goalToIcon(title: String, colors: androidx.compose.material3.ColorScheme): Pair<ImageVector, Color> {
    return when {
        title.contains("Lose", ignoreCase = true) || title.contains("weight", ignoreCase = true) ->
            Icons.Filled.LocalFireDepartment to colors.error // warm red
        title.contains("Build", ignoreCase = true) || title.contains("muscle", ignoreCase = true) ->
            Icons.Filled.FitnessCenter to colors.primary
        title.contains("Endurance", ignoreCase = true) || title.contains("endurance", ignoreCase = true) ->
            Icons.Filled.DirectionsRun to (colors.secondary ?: colors.primary)
        else ->
            Icons.Filled.Favorite to (colors.tertiary ?: colors.primary)
    }
}