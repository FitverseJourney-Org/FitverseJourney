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
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterGender
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.GridOptionCard
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@Composable
fun RegisterPageStepGender(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
        FormHeader(
            title = "Como você se identifica?",
            subtitle = "Essencial para o cálculo da taxa metabólica e macros."
        )

        // Definindo cores vibrantes (Tech Blue e Tech Pink) que fogem do "pastel"
        val genders = listOf(
            GenderItem(
                name = "Masculino",
                icon = Icons.Default.Male,
                color = Color(0xFF2196F3), // Azul Tech vibrante
                registerGender = RegisterGender.MALE
            ),
            GenderItem(
                name = "Feminino",
                icon = Icons.Default.Female,
                color = Color(0xFFE91E63), // Rosa Tech vibrante (quase Magenta)
                registerGender = RegisterGender.FEMALE
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(10.dp),
        ) {
            items(genders) { item ->
                GridOptionCard(
                    text = item.name,
                    icon = item.icon,
                    iconBgColor = item.color.copy(alpha = 0.12f),
                    iconTint = item.color,
                    isSelected = state.registerGender == item.registerGender,
                    onClick = {
                        onAction(RegisterAction.GenderChanged(item.registerGender))
                    }
                )
            }
        }
    }
}

private data class GenderItem(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val registerGender: RegisterGender
)
