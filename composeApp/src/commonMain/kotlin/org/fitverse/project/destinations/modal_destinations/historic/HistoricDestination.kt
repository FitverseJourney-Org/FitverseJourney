package org.fitverse.project.destinations.modal_destinations.historic

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.historic.HistoricRoot
import org.fitverse.presentation.ui.historic.viewmodel.HistoricViewModel

@Composable
fun HistoricDestination(
    viewModel:    HistoricViewModel,
    navigateBack: () -> Unit,
    modifier:     Modifier,
) {
    HistoricRoot(
        viewModel = viewModel,
        onBack    = navigateBack,
        modifier  = modifier,
    )
}
