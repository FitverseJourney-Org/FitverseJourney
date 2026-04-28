package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RestDayView(colors: ColorScheme) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone em Verde Neon (Tertiary) para representar saúde/recuperação
        Icon(Icons.Rounded.Spa, null, modifier = Modifier.size(64.dp), tint = colors.tertiary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("DIA DE DESCANSO", fontWeight = FontWeight.Black, fontSize = 20.sp, color = colors.onBackground)
        Text(
            "Seus músculos crescem durante o repouso. Hidrate-se e recupere seu vigor!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}