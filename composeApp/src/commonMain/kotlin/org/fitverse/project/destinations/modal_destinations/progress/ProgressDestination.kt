package org.fitverse.project.destinations.modal_destinations.progress


import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.progress.ProgressScreen
import com.example.presentation.screens.ui.progress.viewmodel.ProgressViewModel

@Composable
fun ProgressDestination(
    viewmodel: ProgressViewModel,
    toBack: () -> Unit
) {
    ProgressScreen(
        viewModel = viewmodel,
        onBack = toBack
    )
}

