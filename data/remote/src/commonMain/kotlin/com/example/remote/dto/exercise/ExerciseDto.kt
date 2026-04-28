package com.example.remote.dto.exercise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object para exercício.
 *
 * Representa o formato cru dos dados vindos da API REST ou do banco local.
 * Não deve nunca chegar à camada de domínio — o [ExerciseMapper] realiza
 * a conversão para [com.example.domain.progress.model.Exercise].
 *
 * ## Por que ter um DTO separado?
 * - O nome do campo na API pode mudar (`training_split` → `split_letter`)
 *   sem impactar o modelo de domínio.
 * - Campos extras da API (ex.: `created_at`, `server_id`) não poluem o domínio.
 * - Facilita versionamento de schema sem quebrar lógica de negócio.
 *
 * @param id             Identificador único vindo do servidor ou banco local.
 * @param name           Nome completo do exercício.
 * @param muscleGroup    Grupo muscular principal (snake_case vindo da API).
 * @param trainingSplit  Letra/nome da ficha.
 * @param isActive       Flag de soft-delete; exercícios inativos são filtrados
 *                       antes de chegar ao domínio.
 */
@Serializable
data class ExerciseDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("muscle_group")
    val muscleGroup: String,

    @SerialName("training_split")
    val trainingSplit: String,

    @SerialName("is_active")
    val isActive: Boolean = true,
)