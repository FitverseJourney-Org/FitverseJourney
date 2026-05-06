package com.example.presentation.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MissionCard(
    title: String,
    description: String,
    xp: Int?,
    isCompleted: Boolean = false,
    icon: @Composable () -> Unit
) {
    val alpha = if (isCompleted) 0.5f else 1f
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth().alpha(alpha),
        shape = RoundedCornerShape(20.dp),
        color = cs.surface.copy(.5f),
        border = BorderStroke(1.dp, color = Color(0xFF2a2a35))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .border(1.dp, cs.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { icon() }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            if (isCompleted) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = cs.primary
                )
            }else {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier
                            .clickable(role = Role.Button){
                                println("XP ganho: $xp")
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(cs.primary.copy(.75f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        text = "$xp XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}