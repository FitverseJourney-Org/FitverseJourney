package com.example.presentation.screens.ui.main.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.main.dashboard.components.LevelNotification
import com.example.presentation.screens.ui.main.dashboard.components.Notification
import com.example.presentation.screens.ui.main.dashboard.components.NotificationCard

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


    Column(modifier = modifier) {
        // Header (mantido igual)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
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
            Text("Notification", style = MaterialTheme.typography.titleMedium.copy(color = Color.White))
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(notifications) { item ->
                NotificationCard(data = item)
            }
        }
    }
}