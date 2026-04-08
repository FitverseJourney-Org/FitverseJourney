package com.example.presentation.screens.ui.planWorkout

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitverseTopAppBar
import kotlinx.coroutines.delay


// Estados possíveis da tela de IA
sealed class AiGenerationState {
    object Idle : AiGenerationState() // Opcional: para mostrar um botão "Iniciar Geração"
    object Generating : AiGenerationState()
    data class Success(val generatedPlan: GeneratedAiPlan) : AiGenerationState()
    data class Error(val message: String) : AiGenerationState()
}

// Modelo do plano retornado pela IA para feedback visual
data class GeneratedAiPlan(
    val title: String,
    val focus: String,
    val splitType: String, // ex: "ABCD", "Push/Pull/Legs"
    val daysPerWeek: Int,
    val level: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutAiPlanGenerationDestination(
    onBack: () -> Unit,
    onPlanAccepted: () -> Unit
) {
    // Em um cenário real, este estado viria do seu ViewModel (KMP)
    // Aqui estamos simulando a chamada da IA com um LaunchedEffect
    var viewState by remember { mutableStateOf<AiGenerationState>(AiGenerationState.Generating) }

    LaunchedEffect(viewState) {
        // Simula o tempo de resposta da API da IA (ex: Vertex AI, OpenAI)
        delay(3500)
        viewState = AiGenerationState.Success(
            generatedPlan = GeneratedAiPlan(
                title = "Protocolo Hipertrofia Máxima",
                focus = "Hipertrofia e Força",
                splitType = "Divisão ABCD",
                daysPerWeek = 4,
                level = "Intermediário",
                description = "Um plano focado em progressão de carga e volume adequado para seu nível intermediário, visando otimização do dano muscular e recuperação."
            )
        )
    }

    val cs = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            FitverseTopAppBar(title = "GERAR PLANO", onBack = onBack)
        },
        containerColor = cs.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = viewState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "AiGenerationTransition"
            ) { state ->
                when (state) {
                    is AiGenerationState.Generating -> GeneratingFeedback(cs)
                    is AiGenerationState.Success -> AiSuccessFeedback(
                        plan = state.generatedPlan,
                        onAccept = onPlanAccepted,
                        onRegenerate = { viewState = AiGenerationState.Generating } // Re-dispara a simulação/chamada
                    )
                    is AiGenerationState.Error -> Text("Erro na conexão mental com a IA.", color = cs.error)
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun GeneratingFeedback(cs: ColorScheme) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = cs.tertiary,
            strokeWidth = 4.dp,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Forjando seu protocolo...",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = cs.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Analisando histórico, volume e biometria.",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AiSuccessFeedback(
    plan: GeneratedAiPlan,
    onAccept: () -> Unit,
    onRegenerate: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = cs.tertiary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Plano Gerado com Sucesso!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = cs.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card de Resumo do Plano
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = cs.surface,
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = plan.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = cs.tertiary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InfoTag(label = "Foco", value = plan.focus, modifier = Modifier.weight(1f))
                    InfoTag(label = "Divisão", value = plan.splitType, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    InfoTag(label = "Frequência", value = "${plan.daysPerWeek}x na semana", modifier = Modifier.weight(1f))
                    InfoTag(label = "Nível", value = plan.level, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = cs.outline.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Botões de Ação
        Button(
            onClick = onAccept,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = cs.primary)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ACEITAR E SALVAR PROTOCOLO", fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onRegenerate,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.3f))
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = cs.onBackground)
            Spacer(modifier = Modifier.width(8.dp))
            Text("GERAR NOVAMENTE", fontWeight = FontWeight.Bold, color = cs.onBackground)
        }
    }
}

@Composable
private fun InfoTag(label: String, value: String, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = cs.onSurface)
    }
}