package com.example.presentation.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.DarkGamifiedColors

//@Composable
//fun FitverseCardRecommended(
//    workout: WorkoutItem,
//    onClick: () -> Unit
//) {
//    val cs = MaterialTheme.colorScheme
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        onClick = onClick,
//        shape = RoundedCornerShape(20.dp),
//        color = cs.surface.copy(alpha = 0.6f),
//        border = BorderStroke(1.dp, DarkGamifiedColors.PrimarySoft.copy(alpha = 0.15f))
//    ) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Icon Placeholder (Roxo Elétrico)
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(
//                        color = cs.primary.copy(alpha = 0.1f),
//                        shape = RoundedCornerShape(12.dp)
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.FitnessCenter,
//                    contentDescription = null,
//                    tint = cs.primary,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            Spacer(Modifier.width(16.dp))
//
//            Column(Modifier.weight(1f)) {
//                Text(workout.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "${workout.duration} • ",
//                        color = Color.White.copy(alpha = 0.5f),
//                        style = MaterialTheme.typography.labelMedium
//                    )
//                    Text(
//                        text = "+${workout.xp} XP",
//                        color = cs.tertiary,
//                        fontWeight = FontWeight.Black,
//                        fontSize = 12.sp
//                    )
//                }
//            }
//
//            Icon(
//                Icons.Rounded.Bolt,
//                null,
//                tint = if (workout.xp > 150) cs.tertiary else Color.White.copy(alpha = 0.2f),
//                modifier = Modifier.size(20.dp)
//            )
//        }
//    }
//}