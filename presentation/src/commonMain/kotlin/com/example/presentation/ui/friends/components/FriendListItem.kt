package com.example.presentation.ui.friends.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.models.friends.UserProfile

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