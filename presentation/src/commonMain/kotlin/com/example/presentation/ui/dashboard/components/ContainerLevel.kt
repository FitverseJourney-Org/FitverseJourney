package com.example.presentation.ui.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.GeneratingTokens
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.presentation.ui.dashboard.state.AvatarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Shape customizado para o Hexágono do Nível
class HexagonShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(
            Path().apply {
                val w = size.width
                val h = size.height
                moveTo(w * 0.5f, 0f)
                lineTo(w, h * 0.25f)
                lineTo(w, h * 0.75f)
                lineTo(w * 0.5f, h)
                lineTo(0f, h * 0.75f)
                lineTo(0f, h * 0.25f)
                close()
            }
        )
    }
}

@Composable
fun ContainerLevel(
    state: AvatarState,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val xpColor = Color(0xFF42A5F5)

    // 1. Estado para controlar o Dialog
    var showRewardsDialog by remember { mutableStateOf(false) }

    // 2. Chamar o Dialog no topo do Composable
    RewardsInfoDialog(
        visible = showRewardsDialog,
        currentLevel = state.level, // <-- Passando o nível atual do avatar
        onDismiss = { showRewardsDialog = false }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // ... (Conteúdo do Meio mantido igual) ...

            // ... (Footer: Onde está o botão de ajuda) ...
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // XP Total (Mantido)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, null, tint = xpColor, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(text = "${state.xp}", color = cs.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Text(text = "XP Total", color = cs.onSurfaceVariant, fontSize = 12.sp)
                }

                // Botão de Recompensa (Mantido)
                ClaimRewardButton(
                    text = "Recompensa",
                    buttonColor = cs.surface.copy(alpha = 0.5f),
                    textColor = cs.onSurfaceVariant.copy(alpha = 0.5f)
                )

                // Botão de Ajuda ATUALIZADO
                IconButton(
                    onClick = { showRewardsDialog = true }, // <-- Ação que abre o Dialog
                    modifier = Modifier
                        .size(40.dp)
                        .background(cs.surface.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.HelpOutline,
                        contentDescription = "Ajuda Recompensas",
                        tint = cs.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Botão extraído seguindo parâmetros de altura e cor customizável
@Composable
fun ClaimRewardButton(
    text: String,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp), // Altura fixa aplicada
        shape = RoundedCornerShape(12.dp),
        color = buttonColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

// Modelo simples para as recompensas
data class LevelReward(val level: Int, val coins: Int)

@Composable
fun RewardsInfoDialog(
    visible: Boolean,
    currentLevel: Int, // Passamos o nível atual para comparar
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var animateTrigger by remember { mutableStateOf(false) }

    val rewards = remember {
        listOf(
            LevelReward(5, 500),
            LevelReward(10, 1200),
            LevelReward(15, 2500),
            LevelReward(20, 5000),
            LevelReward(25, 7500),
            LevelReward(30, 10000)
        )
    }

    LaunchedEffect(visible) { if (visible) animateTrigger = true }

    val closeWithAnimation = {
        coroutineScope.launch {
            animateTrigger = false
            delay(200)
            onDismiss()
        }
    }

    if (visible) {
        Dialog(onDismissRequest = { closeWithAnimation() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)))

                AnimatedVisibility(
                    visible = animateTrigger,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.9f)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).heightIn(max = 500.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2E3C)),
                        border = BorderStroke(1.dp, Color(0xFF3F4458))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            // Header (Igual ao anterior)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Recompensas", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                                IconButton(onClick = { closeWithAnimation() }) { Icon(Icons.Rounded.Close, null, tint = Color.Gray) }
                            }

                            Spacer(Modifier.height(16.dp))

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                items(rewards) { reward ->
                                    val isUnlocked = currentLevel >= reward.level

                                    // Estilização dinâmica baseada no status
                                    val backgroundColor = if (isUnlocked) Color(0xFFB6FF00).copy(alpha = 0.05f) else Color(0xFF242834)
                                    val contentAlpha = if (isUnlocked) 1f else 0.4f
                                    val borderColor = if (isUnlocked) Color(0xFFB6FF00).copy(alpha = 0.3f) else Color.Transparent

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(backgroundColor, RoundedCornerShape(16.dp))
                                            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Nível ${reward.level}",
                                                color = Color.White.copy(alpha = contentAlpha),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                                Icon(
                                                    Icons.Rounded.GeneratingTokens,
                                                    null,
                                                    tint = if (isUnlocked) Color(0xFFB6FF00) else Color.Gray,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = "${reward.coins} Fitcoins",
                                                    color = if (isUnlocked) Color(0xFFB6FF00) else Color.Gray,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }

                                        // ÍCONE DE STATUS (Check ou Cadeado)
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(
                                                    if (isUnlocked) Color(0xFFB6FF00).copy(alpha = 0.1f)
                                                    else Color.White.copy(alpha = 0.05f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (isUnlocked) Icons.Rounded.CheckCircle else Icons.Rounded.Lock,
                                                contentDescription = null,
                                                tint = if (isUnlocked) Color(0xFFB6FF00) else Color.Gray.copy(alpha = 0.5f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(24.dp))

                            Button(
                                onClick = { closeWithAnimation() },
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D53FF))
                            ) {
                                Text("ENTENDIDO", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}