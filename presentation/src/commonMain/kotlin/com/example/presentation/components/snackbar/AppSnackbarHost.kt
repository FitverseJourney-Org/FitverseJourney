package org.fitverse.presentation.components.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import org.fitverse.domain.models.snackbar.SnackbarType

// Supondo que SnackbarType já exista no seu projeto


/**
 * Host customizado sênior que gerencia a exibição e o design das Snackbars.
 */
@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    snackbarType: SnackbarType, // Idealmente, isso deveria vir de dentro do 'data' customizado, mas mantendo sua API
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.statusBarsPadding().padding(vertical = 24.dp, horizontal = 16.dp)
    ) { data ->
        // Captura o estilo baseado no tipo
        val style = getSnackbarStyle(snackbarType)

        FitverseSnackbar(
            data = data,
            style = style
        )
    }
}

/**
 * O Composable visual da Snackbar, focado em design premium e legibilidade.
 */
@Composable
private fun FitverseSnackbar(
    data: SnackbarData,
    style: SnackbarStyle
) {
    // Surface fornece elevação, sombra e cantos arredondados modernos (16dp)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = style.containerColor,
        tonalElevation = 4.dp,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val defaultLineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            val safeLineHeight = if (defaultLineHeight.isUnspecified) {
                TextUnit.Unspecified
            } else {
                defaultLineHeight * 1.1f
            }
            // 1. Ícone Semântico
            Icon(
                imageVector = style.icon,
                contentDescription = null, // Semântico via cor/texto
                tint = style.contentColor,
                modifier = Modifier.size(24.dp)
            )

            // 2. Texto da Mensagem (com suporte a múltiplas linhas se necessário)
            Text(
                text = data.visuals.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = safeLineHeight // Usamos o valor seguro aqui
                ),
                color = style.contentColor,
                modifier = Modifier.weight(1f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // 3. Botão de Ação (se houver label)
            data.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { data.performAction() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = style.contentColor)
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

/**
 * Data class interna para encapsular o estilo visual.
 */
private data class SnackbarStyle(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector
)

/**
 * Mapeamento sênior utilizando a paleta de cores tonais do Material 3.
 * Isso garante excelente contraste tanto em Light quanto Dark Mode automaticamente.
 */
@Composable
private fun getSnackbarStyle(type: SnackbarType): SnackbarStyle {
    val colors = MaterialTheme.colorScheme

    return when (type) {
        SnackbarType.SUCCESS -> SnackbarStyle(
            // Tons de Verde M3 (Você pode personalizar estas cores no seu Theme.kt)
            containerColor = Color(0xFFC8E6C9), // Verde claro tonal
            contentColor = Color(0xFF1B5E20),   // Verde escuro onContainer
            icon = Icons.Rounded.CheckCircle
        )
        SnackbarType.ERROR -> SnackbarStyle(
            // Padrão M3 para ErrorContainer e OnErrorContainer
            containerColor = colors.errorContainer,
            contentColor = colors.onErrorContainer,
            icon = Icons.Rounded.Error
        )
        SnackbarType.INFO -> SnackbarStyle(
            // Padrão M3 para SecondaryContainer
            containerColor = colors.secondaryContainer,
            contentColor = colors.onSecondaryContainer,
            icon = Icons.Rounded.Info
        )
        SnackbarType.ALERT -> SnackbarStyle(
            // Tons de Amarelo M3 (Customizado)
            containerColor = Color(0xFFFFF9C4), // Amarelo claro tonal
            contentColor = Color(0xFFE65100),   // Laranja escuro onContainer
            icon = Icons.Rounded.Warning
        )
    }
}