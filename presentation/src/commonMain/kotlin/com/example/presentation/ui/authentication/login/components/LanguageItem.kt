package com.example.presentation.ui.authentication.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun LanguageChooser(
    flagRes: DrawableResource,
    code: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(flagRes),
            contentDescription = code
        )

        Text(
            text = code,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White
            )
        )
    }
}