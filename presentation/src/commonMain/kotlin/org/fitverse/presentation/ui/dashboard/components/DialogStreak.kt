package org.fitverse.presentation.ui.dashboard.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.fitverse.presentation.ui.dashboard.util.StreakState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedStreakDialog(
    visible: Boolean,
    streakState: StreakState,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var animateTrigger by remember { mutableStateOf(false) }
    val cs = MaterialTheme.colorScheme

    LaunchedEffect(visible) {
        if (visible) animateTrigger = true
    }

    val triggerClose = {
        coroutineScope.launch {
            animateTrigger = false
            delay(200)
            onDismiss()
        }
    }

    if (visible) {
        Dialog(onDismissRequest = { triggerClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface), // #2A2E3C
                border = BorderStroke(1.dp, cs.outline) // #3F4458
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Título e Ícone
                    Icon(Icons.Rounded.LocalFireDepartment, null, tint = cs.primary, modifier = Modifier.size(60.dp))

                    // Contador com Contraste
                    Text(
                        text = "${streakState.totalStreakCount} Days",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = cs.onBackground
                    )

                    Spacer(Modifier.height(24.dp))
                    content() // O CardStreakWeek entra aqui

                    Spacer(Modifier.height(24.dp))

                    // BOTÃO PRINCIPAL: Neon + Texto Preto (Regra de Ouro)
                    Button(
                        onClick = { triggerClose() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = Color.Black // Máximo contraste
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("BORAA TREINAR!", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}