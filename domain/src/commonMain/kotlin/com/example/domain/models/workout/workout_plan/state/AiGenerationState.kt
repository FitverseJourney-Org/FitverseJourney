package com.example.domain.models.workout.workout_plan.state

import com.example.domain.models.workout.workout_plan.GeneratedAiPlan

sealed class AiGenerationState {
    object Idle : AiGenerationState() // Opcional: para mostrar um botão "Iniciar Geração"
    object Generating : AiGenerationState()
    data class Success(val generatedPlan: GeneratedAiPlan) : AiGenerationState()
    data class Error(val message: String) : AiGenerationState()
}