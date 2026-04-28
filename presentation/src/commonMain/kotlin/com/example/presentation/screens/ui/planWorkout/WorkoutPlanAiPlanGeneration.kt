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
import com.example.domain.models.workout.workout_plan.GeneratedAiPlan
import com.example.domain.models.workout.workout_plan.state.AiGenerationState
import com.example.presentation.screens.ui.planWorkout.components.AiSuccessFeedback
import com.example.presentation.screens.ui.planWorkout.components.GeneratingFeedback
import com.example.presentation.screens.widgets.FitverseTopAppBar
import kotlinx.coroutines.delay


// Estados possíveis da tela de IA

// Modelo do plano retornado pela IA para feedback visual


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





