package org.fitverse.project.destinations.modal_destinations.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.ui.friends.FriendsScreen
import com.example.presentation.screens.ui.friends.viewmodel.FriendsViewModel

@Composable
fun FriendsDestination(
    onBack: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    viewModel: FriendsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FriendsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToQrScanner = onNavigateToQrScanner
    )
}