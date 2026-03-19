package com.example.presentation.screens.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
){

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
            val notificationLevel = if (isLong) _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.LevelNotification.WARNING else _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.LevelNotification.INFO
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.Notification(
                title = if (isLong) "Notificação Longa #${index + 1}" else "Aviso Curto #${index + 1}",
                description = if (isLong) loremIpsum else shortTexts.random(),
                date = "20 de Outubro",
                time = "14:${10 + index}",
                level = notificationLevel
            )
        }
    }


    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(onClick = { onExit() }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription =  null,
                    tint = Color.White
                )
            }
            Text(
                text = "Notification",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                ),

            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(notifications) { item ->
                _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.NotificationCard(
                    data = item
                )
            }
        }
    }
}