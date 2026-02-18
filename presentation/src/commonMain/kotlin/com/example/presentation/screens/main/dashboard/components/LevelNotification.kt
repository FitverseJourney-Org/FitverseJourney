package com.example.presentation.screens.main.dashboard.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.presentation.theme.CardBgDefaultColor


enum class LevelNotification(
    val icon: ImageVector,
){
    INFO(
        icon = Icons.Default.Info
    ),
    WARNING(
        icon = Icons.Default.Warning
    )
}

data class Notification(
    val level: LevelNotification = _root_ide_package_.com.example.presentation.screens.main.dashboard.components.LevelNotification.INFO,
    val title: String,
    val description: String,
    val date: String,
    val time: String
)

@Composable
fun NotificationCard(data: Notification) {
    var isExpanded by remember { mutableStateOf(false) }

    // Estado para saber se o texto realmente ultrapassou o limite
    var isClickable by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize().padding(horizontal = 16.dp, vertical = 8.dp), // Anima a expansão suavemente
        shape = RoundedCornerShape(5.dp),
        onClick = { if (isClickable) isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = CardBgDefaultColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(top = 5.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)){
                    Text(
                        text = data.date,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Green)
                    )
                    Text(
                        text = data.time,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Green)
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Green
                    )
                }
            }

            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = data.level.icon,
                    contentDescription = null,
                    tint = if(data.level == _root_ide_package_.com.example.presentation.screens.main.dashboard.components.LevelNotification.INFO) Color.Green else Color.Yellow
                )
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Texto da Descrição
            Text(
                text = data.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                // Aqui detectamos se o texto é maior que 3 linhas
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded) { // Só checamos enquanto está colapsado
                        isClickable = textLayoutResult.didOverflowHeight
                    }
                }
            )

            // O "Ler mais" só aparece se isClickable for true
            if (isClickable) {
                Text(
                    text = if (isExpanded) "Ver menos" else "Ler mais",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.Green.copy(0.8f),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}