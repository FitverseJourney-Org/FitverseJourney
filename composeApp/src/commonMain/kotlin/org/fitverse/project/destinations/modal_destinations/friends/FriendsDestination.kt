package org.fitverse.project.destinations.modal_destinations.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.widgets.FriendsScreen

@Composable
fun FriendsDestination(
    onBack: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    viewModel: FriendsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FriendsScreen(
        onBack = onBack
    )
}