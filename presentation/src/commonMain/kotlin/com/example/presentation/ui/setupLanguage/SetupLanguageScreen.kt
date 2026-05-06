package com.example.presentation.ui.setupLanguage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.models.local.language.AppLanguageItem
import com.example.presentation.utils.LanguageAvailableApp.Companion.availableAppLanguageItems
import org.jetbrains.compose.resources.painterResource

@Composable
fun SetupLanguageScreen(
    onConfirmLanguage: (AppLanguageItem) -> Unit,
    currentAppLanguageItem: AppLanguageItem,
    exit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var selectedLanguage by remember { mutableStateOf(currentAppLanguageItem) }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            // Header mais refinado com separação visual sutil
            Column(modifier = Modifier.statusBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Idioma",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = colors.onBackground
                        )
                        Text(
                            text = "Selecione sua tradução preferida",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(
                        onClick = exit,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colors.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = colors.onSurface)
                    }
                }
            }
        },
        bottomBar = {
            // Botão com Padding de Segurança para Gestos do Android
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        println("Selected Language: $selectedLanguage")
                        onConfirmLanguage(selectedLanguage)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Confirmar Alteração",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(availableAppLanguageItems) { language ->
                LanguageSelectionCard(
                    appLanguageItem = language,
                    isSelected = language == selectedLanguage,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedLanguage = language
                    }
                )
            }
        }
    }
}

@Composable
private fun LanguageSelectionCard(
    appLanguageItem: AppLanguageItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Animações de estado para suavidade
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.outlineVariant.copy(alpha = 0.4f),
        label = "borderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colors.primaryContainer.copy(alpha = 0.15f) else colors.surface,
        label = "containerColor"
    )
    val iconAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else 0f, label = "iconAlpha")

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo com a bandeira ou sigla (Placeholder para ícone de idioma)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = CircleShape

                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(resource = appLanguageItem.flagRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)

                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appLanguageItem.code.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colors.onSurface
                )
                Text(
                    text = appLanguageItem.name, // Ex: Portuguese / Português
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            // Checkmark animado
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(24.dp).graphicsLayer { alpha = iconAlpha }
                )
            }
        }
    }
}
