package org.fitverse.project.destinations.modal_destinations.wiki

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.wiki.WikiFitnessScreen
import com.example.presentation.ui.wiki.viewmodel.WikiViewModel

@Composable
fun WikiFitnessDestination(
    onNavigateToArticle: (String) -> Unit,
    viewModel: WikiViewModel,
    onBack: () -> Unit,
    modifier: Modifier
) {
    WikiFitnessScreen(
        onBack = onBack,
        modifier = modifier
    )
}