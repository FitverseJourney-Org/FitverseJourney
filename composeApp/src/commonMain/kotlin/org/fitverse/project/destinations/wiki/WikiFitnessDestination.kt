package org.fitverse.project.destinations.wiki

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.wiki.WikiFitnessScreen
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel

@Composable
fun WikiFitnessDestination(
    onNavigateToArticle: (String) -> Unit,
    viewModel: WikiViewModel,
    onBack: () -> Unit
) {
    WikiFitnessScreen(
        onBack = onBack
    )
}