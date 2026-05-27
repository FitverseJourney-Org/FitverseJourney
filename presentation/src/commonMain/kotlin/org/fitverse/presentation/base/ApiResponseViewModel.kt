package org.fitverse.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.fitverse.domain.models.api.ApiResponse

abstract class ApiResponseViewModel<T> : ViewModel() {

    private val _state = MutableStateFlow<ApiResponse<T>>(ApiResponse.Loading)
    val state: StateFlow<ApiResponse<T>> = _state

    // Coleta o Flow emitido pelo use case e publica no StateFlow
    protected fun fetch(flow: Flow<ApiResponse<T>>) {
        viewModelScope.launch {
            flow.collect { _state.value = it }
        }
    }

    // Obrigatório: subclasses definem como recarregar os dados
    abstract fun retry()
}
