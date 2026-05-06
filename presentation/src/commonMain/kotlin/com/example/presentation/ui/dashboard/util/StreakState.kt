package com.example.presentation.ui.dashboard.util

data class StreakState(
    val totalStreakCount: Int = 0,
    val isTodayChecked: Boolean = false,
    val goal: Int = 7
){
    // Retorna quantos bloquinhos devem estar preenchidos no ciclo atual (1 a 7)
    // Se o total é 8, o progresso no ciclo é 1. Se é 7, o progresso é 7.
    val currentCycleProgress: Int
        get() = if (totalStreakCount == 0) 0 else ((totalStreakCount - 1) % goal) + 1

    // Verifica se um bloquinho específico (0 a 6) deve estar marcado
    fun isBlockChecked(blockIndex: Int): Boolean {
        // blockIndex vai de 0 a 6.
        // Se currentCycleProgress for 3, os blocos 0, 1 e 2 estarão marcados.
        return blockIndex < currentCycleProgress
    }
}