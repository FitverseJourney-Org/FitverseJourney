package org.fitverse.project.destinations.modal_destinations.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.progress.ProgressionRoot
import com.example.presentation.ui.progress.viewmodel.ProgressViewModel

@Composable
fun ProgressDestination(
    viewmodel: ProgressViewModel,
    toBack: () -> Unit,
    modifier: Modifier,
) {
    ProgressionRoot(
        viewModel = viewmodel,
        onBack    = toBack,
        modifier  = modifier,
    )
}
