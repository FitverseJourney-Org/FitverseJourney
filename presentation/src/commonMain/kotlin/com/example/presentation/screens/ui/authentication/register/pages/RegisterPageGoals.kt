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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterGoal
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.GridOptionCard
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@Composable
fun RegisterPageGoals(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Qual seu objetivo?",
            subtitle = "Isso define sua rotina e a base do seu plano alimentar."
        )

        // Paleta Vibrante: Força (Vermelho), Queima (Azul), Performance (Roxo), Vitalidade (Verde)
        val goals = listOf(
            GoalItem(
                "Ganho de Massa",
                Icons.Default.TrendingUp,
                Color(0xFFF43F5E),
                RegisterGoal.GAIN_MUSCLE
            ),
            GoalItem(
                "Perda de Peso",
                Icons.Default.Scale,
                Color(0xFF3B82F6),
                RegisterGoal.LOSE_WEIGHT
            ),
            GoalItem(
                "Performance",
                Icons.Default.Speed,
                Color(0xFF8B5CF6),
                RegisterGoal.MAINTENANCE
            ),
            GoalItem("Saúde", Icons.Default.Favorite, Color(0xFF10B981), RegisterGoal.HEALTH)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(goals) { item ->
                GridOptionCard(
                    text = item.title,
                    icon = item.icon,
                    // Fundo do ícone: 15% da cor vibrante
                    iconBgColor = item.color.copy(alpha = 0.15f),
                    // Ícone: Cor sólida vibrante para contraste total
                    iconTint = item.color,
                    isSelected = state.registerGoal == item.registerGoal,
                    onClick = { onAction(RegisterAction.GoalChanged(item.registerGoal)) }
                )
            }
        }
    }
}

private data class GoalItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val registerGoal: RegisterGoal
)