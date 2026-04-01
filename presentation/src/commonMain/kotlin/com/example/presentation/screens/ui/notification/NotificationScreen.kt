package com.example.presentation.screens.ui.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.dashboard.components.LevelNotification
import com.example.presentation.screens.ui.dashboard.components.Notification
import com.example.presentation.screens.ui.dashboard.components.NotificationCard
import com.example.presentation.screens.widgets.FitverseIconBack

@Composable
fun NotificationMainScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Usando o Surface (#16171D) para o topo e Background (#0A0B0F) para o fundo


    val notifications = remember {
        // ... (lógica de mock mantida)
        List(10) { index ->
            Notification(
                title = "System Log #${index + 1}",
                description = "Sua missão diária foi atualizada. Ganhe +500 XP.",
                date = "20 OCT",
                time = "14:${10 + index}",
                level = LevelNotification.entries.random()
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize()// Gradiente dinâmico baseado no tema
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FitverseIconBack(
                    onBack = onExit
                )
                Text(
                    text = "NOTIFICATIONS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = colors.onBackground, // Branco Puro
                    letterSpacing = 1.sp
                )
            }

            // Badge de Contagem (Estilo Gamer usando o Tertiary Green)
            Surface(
                color = colors.tertiary.copy(alpha = 0.15f), // Fundo suave do verde neon
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colors.tertiary.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "${notifications.size} NEW",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.tertiary, // Texto em Verde Neon (#10B981)
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- LISTA DE NOTIFICAÇÕES ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(notifications) { item ->
                // O NotificationCard deve ser refatorado internamente para usar:
                // colors.surfaceVariant para o fundo do card (#16171D)
                // colors.primary para ícones de sistema
                NotificationCard(data = item)
            }
        }
    }
}