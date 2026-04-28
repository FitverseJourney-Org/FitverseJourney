package com.example.presentation.screens.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.components.ShapeButton

@Composable
fun FitverseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val cs = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = ShapeButton,
        colors = ButtonDefaults.buttonColors(
            containerColor         = if(isLoading) cs.primary.copy(alpha = 0.85f) else cs.primary,
            contentColor           = cs.background,
            disabledContainerColor = FitverseColors.Surface2,
            disabledContentColor   = FitverseColors.TextMuted,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp),
    ) {
        if(!isLoading){
            Text(
                text       = text.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 16.sp,
                letterSpacing = 1.sp,
                color      = cs.background,
            )
        }else {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        }
    }
}