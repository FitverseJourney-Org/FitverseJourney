package com.example.presentation.ui.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.models.friends.FriendsError
import com.example.domain.models.friends.FriendsEvent
import com.example.domain.models.friends.FriendsUiState
import com.example.presentation.ui.friends.components.AddFriendSection
import com.example.presentation.ui.friends.components.FriendListItem
import com.example.presentation.ui.friends.components.FriendsListHeader
import com.example.presentation.ui.friends.components.SuggestionsSection
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.action_ok
import fitversejourneyapp.presentation.generated.resources.cd_navigate_back
import fitversejourneyapp.presentation.generated.resources.cd_qr_code_scanner
import fitversejourneyapp.presentation.generated.resources.empty_friends_message
import fitversejourneyapp.presentation.generated.resources.empty_friends_title
import fitversejourneyapp.presentation.generated.resources.error_network
import fitversejourneyapp.presentation.generated.resources.error_title
import fitversejourneyapp.presentation.generated.resources.error_unknown
import fitversejourneyapp.presentation.generated.resources.error_user_not_found
import fitversejourneyapp.presentation.generated.resources.friends_title
import org.jetbrains.compose.resources.stringResource

/**
 * Screen principal seguindo padrões de:
 * - State Hoisting
 * - Unidirectional Data Flow
 * - Separation of Concerns
 */


/**
 * Composable stateless e testável
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    uiState: FriendsUiState,
    onEvent: (FriendsEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Error handling
    uiState.error?.let { error ->
        ErrorDialog(
            error = error,
            onDismiss = { onEvent(FriendsEvent.DismissError) }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            FriendsTopBar(
                onBack = onBack,
                onQrCodeClick = onNavigateToQrScanner
            )
        },
        snackbarHost = { SnackbarHost(hostState = remember { SnackbarHostState() }) }
    ) { paddingValues ->

        if (!uiState.isLoading) {
            LoadingState(modifier = Modifier.padding(paddingValues))
        } else {
            FriendsContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun FriendsContent(
    uiState: FriendsUiState,
    onEvent: (FriendsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Add Friend Section
        item(key = "add_friend_card") {
            AddFriendSection(
                friendCode = uiState.friendCode,
                onCodeChange = { onEvent(FriendsEvent.UpdateFriendCode(it)) },
                onAddClick = { onEvent(FriendsEvent.AddFriendByCode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // Suggestions Section
        if (uiState.suggestions.isNotEmpty()) {
            item(key = "suggestions_section") {
                SuggestionsSection(
                    suggestions = uiState.suggestions,
                    onAddClick = { userId -> onEvent(FriendsEvent.AddSuggestion(userId)) }
                )
            }
        }

        // Friends List Section
        item(key = "friends_header") {
            FriendsListHeader(
                friendsCount = uiState.friends.size,
                sortOrder = uiState.sortOrder,
                onSortClick = { onEvent(FriendsEvent.ToggleSortOrder) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Friends List Items
        items(
            items = uiState.friends,
            key = { it.id }
        ) { friend ->
            FriendListItem(
                user = friend,
                modifier = Modifier
                    .fillMaxWidth()
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp
            )
        }

        // Empty State
        if (uiState.friends.isEmpty() && !uiState.isLoading) {
            item(key = "empty_state") {
                EmptyFriendsState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendsTopBar(
    onBack: () -> Unit,
    onQrCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Text(
                text = stringResource(Res.string.friends_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = stringResource(Res.string.cd_navigate_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onQrCodeClick) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = stringResource(Res.string.cd_qr_code_scanner)
                )
            }
        },
        windowInsets = WindowInsets(0,0,0,0),
    )
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorDialog(
    error: FriendsError,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.error_title)) },
        text = {
            Text(
                when (error) {
                    is FriendsError.InvalidCode -> error.message
                    is FriendsError.NetworkError -> stringResource(Res.string.error_network)
                    is FriendsError.UserNotFound -> stringResource(
                        Res.string.error_user_not_found,
                        error.code
                    )
                    is FriendsError.Unknown -> stringResource(Res.string.error_unknown)
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.action_ok))
            }
        }
    )
}

@Composable
private fun EmptyFriendsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.PeopleOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.empty_friends_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.empty_friends_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}