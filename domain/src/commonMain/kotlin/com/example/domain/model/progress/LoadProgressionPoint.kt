package com.example.domain.model.progress

import androidx.compose.runtime.Immutable

/**
 * Ponto de dado de uma sessão de treino para um exercício específico.
 *
 * Cada instância representa uma sessão registrada, carregando a carga utilizada
 * e o 1RM estimado pela fórmula de Epley: `weight × (1 + reps / 30)`.
 *
 * A lista é assumida **ordenada cronologicamente** (mais antiga → mais recente)
 * em todos os casos de uso que a consomem.
 *
 * @param date           Data da sessão (tipo KMP-safe via expect/actual).
 * @param weight         Carga máxima utilizada na sessão (kg).
 * @param estimatedOneRm 1RM estimado em kg.
 */
@Immutable
data class LoadProgressionPoint(
    val date: PlatformDate,
    val weight: Double,
    val estimatedOneRm: Double,
)
