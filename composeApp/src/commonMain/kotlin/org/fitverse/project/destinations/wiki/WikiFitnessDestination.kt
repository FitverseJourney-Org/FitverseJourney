package org.fitverse.project.destinations.wiki

import androidx.compose.runtime.Composable
import com.example.presentation.ui.wiki.WikiFitnessScreen
import com.example.presentation.ui.wiki.viewmodel.WikiViewModel

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