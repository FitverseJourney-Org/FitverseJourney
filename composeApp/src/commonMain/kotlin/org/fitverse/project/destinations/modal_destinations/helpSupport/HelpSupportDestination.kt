package org.fitverse.project.destinations.modal_destinations.helpSupport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.presentation.ui.helpSupport.SupportScreen

@Composable
fun HelpSupportDestination(toBack: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(cs.surface, cs.background)
                )
            )
    ) {
        SupportScreen(
            onBack = toBack
        )
    }
}