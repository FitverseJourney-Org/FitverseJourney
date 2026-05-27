package org.fitverse.project.destinations.modal_destinations.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.widgets.FitverseTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingDestination(toBack: () -> NavKey?, modifier: Modifier) {
    Scaffold(
        modifier            = modifier,
        containerColor      = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title  = "LOJA",
                onBack = { toBack() },
            )
        },
    ) { padding ->
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            MaintenanceContent()
        }
    }
}

@Composable
private fun MaintenanceContent() {
    Column(
        modifier                = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment     = Alignment.CenterHorizontally,
        verticalArrangement     = Arrangement.spacedBy(24.dp),
    ) {
        // Ícone de manutenção
        Box(
            modifier         = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(FitColors.Accent.copy(alpha = 0.08f))
                .border(1.dp, FitColors.Accent.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector        = Icons.Rounded.Build,
                contentDescription = null,
                tint               = FitColors.Accent,
                modifier           = Modifier.size(46.dp),
            )
        }

        // Título
        Text(
            text          = "EM MANUTENÇÃO",
            color         = FitColors.TextPrimary,
            fontSize      = 22.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 1.sp,
            textAlign     = TextAlign.Center,
        )

        // Card de descrição
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(FitColors.Surface2)
                .border(1.dp, FitColors.Outline, RoundedCornerShape(18.dp))
                .padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text       = "A loja está temporariamente indisponível.",
                color      = FitColors.TextMuted,
                fontSize   = 14.sp,
                textAlign  = TextAlign.Center,
                lineHeight = 22.sp,
            )
            Text(
                text       = "Estamos trabalhando para trazer novidades em breve.",
                color      = FitColors.TextDisabled,
                fontSize   = 12.sp,
                textAlign  = TextAlign.Center,
                lineHeight = 18.sp,
            )
        }

        // Badge "EM BREVE"
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(FitColors.Accent.copy(alpha = 0.10f))
                .border(1.dp, FitColors.Accent.copy(alpha = 0.30f), RoundedCornerShape(30.dp))
                .padding(horizontal = 18.dp, vertical = 9.dp),
        ) {
            Text(
                text          = "EM BREVE",
                color         = FitColors.Accent,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 1.5.sp,
            )
        }
    }
}
