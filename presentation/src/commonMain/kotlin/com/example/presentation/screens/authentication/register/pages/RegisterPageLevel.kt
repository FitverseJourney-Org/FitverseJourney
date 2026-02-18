package com.example.presentation.screens.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.presentation.states.authentication.RegisterState

@Composable
fun RegisterPageLevel(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "What’s your training level?",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "This helps us personalize your workouts.",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(32.dp))

        TrainingLevelSelector(
            selectedLevel = state.trainingLevel,
            onLevelSelected = {
                onAction(RegisterAction.TrainingLevelChanged(it))
            }
        )
    }
}
@Composable
fun TrainingLevelSelector(
    selectedLevel: TrainingLevel?,
    onLevelSelected: (TrainingLevel) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.TrainingLevelItem(
            title = "Beginner",
            description = "New to training or getting back on track",
            isSelected = selectedLevel == TrainingLevel.BEGINNER,
            onClick = {
                onLevelSelected(TrainingLevel.BEGINNER)
            }
        )

        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.TrainingLevelItem(
            title = "Intermediate",
            description = "You train regularly and know the basics",
            isSelected = selectedLevel == TrainingLevel.INTERMEDIATE,
            onClick = {
                onLevelSelected(TrainingLevel.INTERMEDIATE)
            }
        )

        _root_ide_package_.com.example.presentation.screens.authentication.register.pages.TrainingLevelItem(
            title = "Advanced",
            description = "High experience and intense training routine",
            isSelected = selectedLevel == TrainingLevel.ADVANCED,
            onClick = {
                onLevelSelected(TrainingLevel.ADVANCED)
            }
        )

    }
}
@Composable
fun TrainingLevelItem(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(backgroundColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = description,
            color = Color.LightGray
        )
    }
}
