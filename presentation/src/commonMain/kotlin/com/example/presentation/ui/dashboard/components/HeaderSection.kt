package org.fitverse.presentation.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.theme.FVTypography
import org.fitverse.presentation.widgets.FitverseTextButton

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text  = title.uppercase(),
            style = FVTypography.overline,
            color = cs.onSurfaceVariant,
        )
        if(actionText != null) {
            FitverseTextButton(
                text  = actionText,
                onClick = { onActionClick?.invoke() },
                style = FVTypography.labelMedium.copy(color = cs.primary),
            )
        }
    }


}