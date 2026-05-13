package org.fitverse.project.destinations.modal_destinations.friends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.ui.friends.FriendsScreen
import com.example.presentation.ui.friends.viewmodel.FriendsViewModel

@Composable
fun FriendsDestination(
    onBack: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    viewModel: FriendsViewModel,
    modifier: Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FriendsScreen(
        onBack = onBack,
        modifier = modifier
    )
}