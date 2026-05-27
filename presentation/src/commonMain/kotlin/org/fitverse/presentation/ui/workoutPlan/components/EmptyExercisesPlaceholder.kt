package org.fitverse.presentation.ui.workoutPlan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────────────────────
// EmptyExercisesPlaceholder
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Estado vazio com botão para abrir o seletor de templates.
 * Stateless — não decide quando abrir o sheet, apenas dispara [onUseTemplate].
 */
@Composable
fun EmptyExercisesPlaceholder(
    colors: ColorScheme,
    onUseTemplate: () -> Unit
) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector        = Icons.Rounded.FitnessCenter,
            contentDescription = null,
            modifier           = Modifier.size(56.dp),
            tint               = colors.onSurfaceVariant.copy(alpha = 0.15f)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text          = "NENHUMA ATIVIDADE",
            style         = MaterialTheme.typography.labelLarge,
            fontWeight    = FontWeight.Black,
            color         = colors.onSurfaceVariant.copy(alpha = 0.3f),
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text      = "Adicione exercícios ou use um template",
            style     = MaterialTheme.typography.bodySmall,
            color     = colors.onSurfaceVariant.copy(alpha = 0.2f),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        OutlinedButton(
            onClick        = onUseTemplate,
            shape          = RoundedCornerShape(14.dp),
            border         = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp,
                brush = SolidColor(colors.primary.copy(alpha = 0.6f))
            ),
            colors         = ButtonDefaults.outlinedButtonColors(
                contentColor   = colors.primary,
                containerColor = colors.primary.copy(alpha = 0.08f)
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector        = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text          = "USAR TEMPLATE",
                fontWeight    = FontWeight.Black,
                fontSize      = 13.sp,
                letterSpacing = 1.sp
            )
        }
    }
}