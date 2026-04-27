package com.example.remote.dto.progression

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object para um ponto de progressão de carga.
 *
 * Armazena os dados exatamente como chegam da API ou do banco local,
 * usando tipos primitivos que são fáceis de serializar/deserializar.
 *
 * A data é representada como [Long] (epoch millis) para ser agnóstica à
 * plataforma no KMP — o [ProgressionMapper] converte para [PlatformDate].
 *
 * @param id             ID único do registro.
 * @param exerciseId     FK para o exercício ao qual pertence.
 * @param epochMillis    Data da sessão como timestamp Unix em milissegundos.
 * @param weightKg       Carga utilizada (kg).
 * @param estimatedOneRm 1RM estimado pela fórmula de Epley (kg).
 * @param reps           Repetições realizadas com a carga máxima (para auditoria).
 * @param sets           Séries realizadas (para auditoria).
 */
@Serializable
data class ProgressionPointDto(
    @SerialName("id")
    val id: String,

    @SerialName("exercise_id")
    val exerciseId: String,

    @SerialName("epoch_millis")
    val epochMillis: Long,

    @SerialName("weight_kg")
    val weightKg: Double,

    @SerialName("estimated_one_rm")
    val estimatedOneRm: Double,

    @SerialName("reps")
    val reps: Int = 0,

    @SerialName("sets")
    val sets: Int = 0,
)