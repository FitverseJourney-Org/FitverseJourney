package com.example.presentation.screens.ui.onboarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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

    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OnboardingIndicator(
            current = currentPage,
            total = totalPages
        )

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = {
                if (currentPage == totalPages - 1) {
                    onFinish()
                } else {
                    onNext()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.primary,
                contentColor = cs.onPrimary
            )
        ) {
            Text(
                text = if (currentPage == totalPages - 1)
                    "Get Started"
                else
                    "Next",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSkip
        ) {
            Text(
                text = "Skip",
                color = cs.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}