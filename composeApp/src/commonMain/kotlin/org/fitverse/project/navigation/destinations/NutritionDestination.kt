package org.fitverse.project.navigation.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.nutrition.NutritionScreenV3

@Composable
fun NutritionDestination() {
    Box(modifier = Modifier.fillMaxSize()){
        PremiumGamifiedBackground()
        NutritionScreenV3()
    }
}