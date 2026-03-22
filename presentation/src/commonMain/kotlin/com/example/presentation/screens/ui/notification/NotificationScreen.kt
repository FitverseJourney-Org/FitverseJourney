package com.example.presentation.screens.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.dashboard.components.LevelNotification
import com.example.presentation.screens.ui.dashboard.components.Notification
import com.example.presentation.screens.ui.dashboard.components.NotificationCard

@Composable
fun NotificationMainScreen(
    modifier: Modifier,
    onExit: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Gradiente de fundo frio para a tela toda
    val screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF242834), Color(0xFF161922))
    )

    // 1. Lista com descrições de tamanhos variados

    val notifications = remember {

        val loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."

        val shortTexts = listOf(

            "Seu pedido foi entregue com sucesso!",

            "Você tem uma nova mensagem de segurança.",

            "Lembrete: Sua aula começa em 15 minutos."

        )



        List(10) { index ->

            val isLong = (0..1).random() == 1 // Sorteio simples (50% de chance)

            val notificationLevel = if (isLong) LevelNotification.WARNING else LevelNotification.INFO

            Notification(

                title = if (isLong) "Notificação Longa #${index + 1}" else "Aviso Curto #${index + 1}",

                description = if (isLong) loremIpsum else shortTexts.random(),

                date = "20 de Outubro",

                time = "14:${10 + index}",

                level = notificationLevel

            )

        }

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(screenGradient) // Aplica o fundo premium
    ) {
        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onExit) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Notificações",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            // Pequeno badge indicando o total
            Surface(
                color = Color(0xFF7D53FF).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${notifications.size}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF7D53FF),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { item ->
                NotificationCard(data = item)
            }
        }
    }
}