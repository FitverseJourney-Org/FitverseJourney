package com.example.presentation.screens.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetSystemLocaleUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val getAppLanguageUseCase    : GetAppLanguageUseCase,
    private val changeAppLanguageUseCase : ChangeAppLanguageUseCase,
    private val getSystemLocaleUseCase   : GetSystemLocaleUseCase,
) : ViewModel() {

    /**
     * Idioma atual como StateFlow.
     * initialValue usa o locale do SO para evitar flash de idioma errado
     * antes do DataStore emitir o primeiro valor.
     */
    val languageCode: StateFlow<String> = getAppLanguageUseCase()
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = getSystemLocaleUseCase(),
        )

    fun switchLanguage(newLanguageCode: String) {
        viewModelScope.launch {
            changeAppLanguageUseCase(newLanguageCode)
        }
    }
}