package com.example.domain.repository

import com.example.domain.model.progress.Exercise
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso a exercícios.
 *
 * ## Regras de Clean Architecture
 * - Esta interface pertence à camada de **domínio** e define **o que** é necessário,
 *   sem saber **como** os dados são obtidos (banco local, API, cache).
 * - A implementação concreta ([ExerciseRepositoryImpl]) vive na camada de dados
 *   e é injetada via DI, nunca referenciada diretamente pela camada de domínio.
 *
 * Todos os métodos retornam [Flow] para suportar atualizações reativas
 * (ex.: uma inserção no banco local reflete automaticamente na UI).
 */
interface ExerciseRepository {

    /**
     * Emite a lista completa de exercícios cadastrados.
     * Reemite sempre que o banco local for alterado.
     */
    fun getAllExercises(): Flow<List<Exercise>>

    /**
     * Emite somente os exercícios da [trainingSplit] informada.
     * Reemite quando exercícios desta ficha forem adicionados/removidos.
     *
     * @param trainingSplit Identificador da ficha (ex.: "A", "Empurrar").
     */
    fun getExercisesByTrainingSplit(trainingSplit: String): Flow<List<Exercise>>

    /**
     * Emite a lista de fichas distintas existentes, ordenadas alfabeticamente.
     * Útil para popular o [SplitTabRow] sem carregar todos os exercícios.
     */
    fun getTrainingSplits(): Flow<List<String>>

    /**
     * Retorna um único exercício pelo seu [id], ou `null` se não existir.
     */
    suspend fun getExerciseById(id: String): Exercise?
}
