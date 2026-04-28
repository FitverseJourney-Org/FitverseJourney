package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CreatePlanMethodDialog(
    onDismiss: () -> Unit,
    onManualSelected: () -> Unit,
    onAiSelected: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = cs.background,
            border = BorderStroke(
                width = 1.dp,
                color = cs.outline.copy(alpha = 0.1f)
            ),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NOVO PLANO DE BATALHA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = cs.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Como você deseja forjar seu próximo treino?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Opção IA
                PlanMethodCard(
                    title = "Geração com IA",
                    subtitle = "Um plano otimizado em segundos.",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = cs.tertiary, // Cor de destaque para a IA
                    onClick = onAiSelected
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Aviso de Privacidade / Uso de Dados (Integrado visualmente abaixo da opção IA)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cs.tertiaryContainer.copy(alpha = 0.3f),
                    border = BorderStroke(1.dp, cs.tertiary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Aviso de IA",
                            tint = cs.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "A IA utilizará seu histórico, medidas e perfil biocorporal para gerar uma rotina sob medida. Seus dados estão seguros.",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurfaceVariant,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Opção Manual
                PlanMethodCard(
                    title = "Criação Manual",
                    subtitle = "Você no controle de cada série e repetição.",
                    icon = Icons.Default.Edit,
                    iconTint = cs.primary,
                    onClick = onManualSelected
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onDismiss) {
                    Text("CANCELAR", fontWeight = FontWeight.Bold, color = cs.secondary)
                }
            }
        }
    }
}