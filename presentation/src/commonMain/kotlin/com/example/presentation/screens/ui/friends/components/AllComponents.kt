package com.example.presentation.screens.ui.friends.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.domain.models.friends.SortOrder
import com.example.domain.models.friends.UserProfile
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.action_add
import fitversejourneyapp.presentation.generated.resources.add_friend_code_hint
import fitversejourneyapp.presentation.generated.resources.add_friend_title
import fitversejourneyapp.presentation.generated.resources.cd_add_friend
import fitversejourneyapp.presentation.generated.resources.cd_sort_ascending
import fitversejourneyapp.presentation.generated.resources.cd_sort_descending
import fitversejourneyapp.presentation.generated.resources.friends_list_title
import fitversejourneyapp.presentation.generated.resources.mutual_connections
import fitversejourneyapp.presentation.generated.resources.suggestions_title
import org.jetbrains.compose.resources.stringResource

/**
 * Seção de adicionar amigo com validação e feedback
 */
@Composable
fun AddFriendSection(
    friendCode: String,
    onCodeChange: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.add_friend_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = friendCode,
                    onValueChange = onCodeChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(stringResource(Res.string.add_friend_code_hint))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (friendCode.isNotBlank()) onAddClick()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                FilledTonalIconButton(
                    onClick = {
                        keyboardController?.hide()
                        onAddClick()
                    },
                    enabled = friendCode.isNotBlank(),
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = stringResource(Res.string.cd_add_friend)
                    )
                }
            }
        }
    }
}

/**
 * Seção de sugestões com LazyRow otimizada
 */
@Composable
fun SuggestionsSection(
    suggestions: List<UserProfile>,
    onAddClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.suggestions_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = suggestions,
                key = { it.id }
            ) { suggestion ->
                SuggestionCard(
                    user = suggestion,
                    onAddClick = { onAddClick(suggestion.id) }
                )
            }
        }
    }
}

/**
 * Card de sugestão otimizado
 */
@Composable
private fun SuggestionCard(
    user: UserProfile,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserAvatar(
                initials = user.initials,
                avatarUrl = user.avatarUrl,
                size = 48.dp
            )

            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    Res.string.mutual_connections,
                    user.mutualConnections
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.action_add),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

/**
 * Header da lista de amigos com sorting
 */
@Composable
fun FriendsListHeader(
    friendsCount: Int,
    sortOrder: SortOrder,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.friends_list_title, friendsCount),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        SortButton(
            sortOrder = sortOrder,
            onClick = onSortClick
        )
    }
}

/**
 * Botão de ordenação acessível
 */
@Composable
private fun SortButton(
    sortOrder: SortOrder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sortDescription = stringResource(
        if (sortOrder == SortOrder.ASCENDING) {
            Res.string.cd_sort_descending
        } else {
            Res.string.cd_sort_ascending
        }
    )

    Surface(
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = sortDescription
        },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sortOrder.label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = if (sortOrder == SortOrder.ASCENDING) {
                    Icons.Default.ArrowUpward
                } else {
                    Icons.Default.ArrowDownward
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Item de lista de amigo otimizado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendListItem(
    user: UserProfile,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = user.name,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = user.username,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            UserAvatar(
                initials = user.initials,
                avatarUrl = user.avatarUrl,
                size = 48.dp
            )
        },
        trailingContent = {
            StreakIndicator(
                streak = user.streak,
                modifier = Modifier.semantics {
                    contentDescription = "${user.name} has ${user.streak} day streak"
                }
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/**
 * Avatar com suporte a imagem e fallback
 */
@Composable
fun UserAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Indicador de streak gamificado
 */
@Composable
private fun StreakIndicator(
    streak: Int,
    modifier: Modifier = Modifier
) {
    if (streak > 0) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = streak.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
    }
}