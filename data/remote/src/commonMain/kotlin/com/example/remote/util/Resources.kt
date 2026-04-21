package com.example.remote.util


// ============================================================
// 10. RESOURCE WRAPPER (Result/State pattern)
// ============================================================
// Arquivo: data/src/commonMain/kotlin/com/example/data/util/Resource.kt

/**
 * Wrapper para representar estados de operações assíncronas
 * Similar ao Result, mas com estado de Loading
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

// Extension functions úteis
fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success
fun <T> Resource<T>.isError(): Boolean = this is Resource.Error
fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading

// ============================================================
// Alternative: DataState pattern (mais completo)
// ============================================================

sealed class DataState<out T> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error"
    ) : DataState<Nothing>()
}