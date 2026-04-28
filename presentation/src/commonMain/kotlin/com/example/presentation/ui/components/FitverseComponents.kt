package com.example.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors

// ── Shapes ────────────────────────────────────────────────────────────────────

val ShapeCard   = RoundedCornerShape(14.dp)
val ShapeButton = RoundedCornerShape(10.dp)
val ShapeSmall  = RoundedCornerShape(8.dp)
val ShapeTag    = RoundedCornerShape(8.dp)

// ── Primary CTA button ────────────────────────────────────────────────────────



// ── Ghost / outline button ────────────────────────────────────────────────────

// ── Text field ────────────────────────────────────────────────────────────────


// ── Top bar with back arrow ───────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitverseTopBar(
    onBack: () -> Unit,
    title: String? = null,
) {
    TopAppBar(
        title = {
            if (title != null) {
                FitverseScreenTitle(title = title)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        windowInsets = WindowInsets(0,0,0,0),
        navigationIcon = {
            IconButton(
                onClick  = onBack,
                modifier = Modifier,
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint               = FitverseColors.TextPrimary,
                )
            }
        }
    )
}

// ── Screen title block ────────────────────────────────────────────────────────

@Composable
fun FitverseScreenTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text       = title.uppercase(),
            fontSize   = 36.sp,
            color      = FitverseColors.TextPrimary,
        )
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text  = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = FitverseColors.TextMuted,
            )
        }
    }
}

// ── Section label ─────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text      = text.uppercase(),
        style     = MaterialTheme.typography.labelSmall,
        color     = FitverseColors.TextMuted,
        letterSpacing = 1.2.sp,
        modifier  = modifier.padding(top = 20.dp, bottom = 10.dp),
    )
}