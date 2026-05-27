package org.fitverse.domain.usecase.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.fitverse.domain.models.api.ApiResponse
import org.fitverse.domain.models.api.toApiResponse

// ─── Use case com parâmetro ───────────────────────────────────────────────────

abstract class ApiResponseUseCase<in P, out T> {
    abstract suspend fun execute(params: P): Result<T>

    operator fun invoke(params: P): Flow<ApiResponse<T>> = flow {
        emit(ApiResponse.Loading)
        emit(execute(params).toApiResponse())
    }
}

// ─── Use case sem parâmetro ───────────────────────────────────────────────────

abstract class ApiResponseUseCaseNoParams<out T> {
    abstract suspend fun execute(): Result<T>

    operator fun invoke(): Flow<ApiResponse<T>> = flow {
        emit(ApiResponse.Loading)
        emit(execute().toApiResponse())
    }
}
