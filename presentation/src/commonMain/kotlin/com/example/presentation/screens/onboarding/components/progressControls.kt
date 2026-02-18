package com.example.presentation.screens.onboarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentGreen

@Composable
fun OnboardingControls(
    modifier: Modifier = Modifier,
    currentPage: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OnboardingIndicator(
            current = currentPage,
            total = totalPages
        )

        // Botão principal
        Button(
            onClick = {
                if (currentPage == totalPages - 1) {
                    onFinish()
                } else {
                    onNext()
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (currentPage == totalPages - 1)
                    "Get Started"
                else
                    "Next",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Skip
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSkip
        ) {
            Text(
                text = "Skip",
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}