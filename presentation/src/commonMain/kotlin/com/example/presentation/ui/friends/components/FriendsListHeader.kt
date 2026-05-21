package org.fitverse.presentation.ui.friends.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fitverse.domain.models.friends.SortOrder
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.cd_sort_ascending
import fitversejourneyapp.presentation.generated.resources.cd_sort_descending
import fitversejourneyapp.presentation.generated.resources.friends_list_title
import org.jetbrains.compose.resources.stringResource

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
fun SortButton(
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