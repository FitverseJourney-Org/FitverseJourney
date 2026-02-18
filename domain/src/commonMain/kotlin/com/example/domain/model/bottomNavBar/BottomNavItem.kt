package com.example.domain.model.bottomNavBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    companion object {
        val items = listOf(
            BottomNavItem(label = "Início", icon = Icons.Default.Home, route = "main/dashboard"),
            BottomNavItem(label = "Treino", icon = Icons.Default.FitnessCenter, route = "main/workout"),
            BottomNavItem(label = "Nutrição", icon = Icons.Default.Restaurant, route = "main/nutrition"),
            BottomNavItem(label = "Perfil", icon = Icons.Default.Person, route = "main/profile")
        )
    }
}