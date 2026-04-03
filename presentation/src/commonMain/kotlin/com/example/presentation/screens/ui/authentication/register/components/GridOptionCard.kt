package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GridOptionCard(
    text: String,
    icon: ImageVector? = null,
    iconResource: DrawableResource? = null,
    iconBgColor: Color,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    openDialogStatusAvatar: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val brandPurple = Color(0xFF4F46E5)

    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    val containerColor by animateColorAsState(
        if (isSelected) brandPurple.copy(alpha = 0.08f) else colors.surfaceVariant.copy(alpha = 0.4f),
        label = "color"
    )

    Surface(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .fillMaxWidth()
            .aspectRatio(1.05f)
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) brandPurple else colors.outlineVariant.copy(alpha = 0.2f)
        ),
        color = containerColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) iconTint.copy(alpha = 0.2f) else iconBgColor,
                    modifier = Modifier.size(60.dp)
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) brandPurple else iconTint,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else if (iconResource != null) {
                        Image(
                            painter = painterResource(resource = iconResource),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                if (text.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = text.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) brandPurple else colors.onSurface,
                        letterSpacing = 1.sp
                    )
                }
            }

            if(iconResource != null){
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp),
                    onClick = {
                        openDialogStatusAvatar()
                    },
                ){
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = colors.onSurface
                    )
                }
            }
        }


    }
}