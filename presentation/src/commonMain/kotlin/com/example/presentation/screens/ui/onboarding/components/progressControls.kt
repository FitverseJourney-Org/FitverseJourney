package com.example.presentation.screens.ui.onboarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitVerseButton

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
    val buttonText = if (currentPage == totalPages - 1) "Get Started" else "Next"
    val buttonOnClick = if (currentPage == totalPages - 1) onFinish else onNext
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

//        FitVerseButton(
//            text = buttonText,
//            // Lógica de clique transferida para cá
//            onClick = buttonOnClick,
//            modifier = Modifier.fillMaxWidth(), // Agora funciona sem conflito
//            // Mapeamento de cores do seu sistema (cs)
//            topColor = cs.primary,
//            edgeColor = cs.outline, // Ou uma cor mais escura que o primary
//            textColor = cs.onPrimary,
//            textStyle = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
//
//        Spacer(Modifier.height(10.dp))
//
//        TextButton(
//            modifier = Modifier.fillMaxWidth(),
//            onClick = onSkip
//        ) {
//            Text(
//                text = "Skip",
//                color = cs.onBackground.copy(alpha = 0.7f)
//            )
//        }
    }
}