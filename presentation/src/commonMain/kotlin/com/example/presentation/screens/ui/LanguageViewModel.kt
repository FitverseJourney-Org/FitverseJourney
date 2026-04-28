package com.example.presentation.screens.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetLocaleLanguageAppUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val appLanguageRepository: AppLanguageRepository,
    private val getAppLanguageUseCase : GetAppLanguageUseCase,
    private val changeAppLanguageUseCase: ChangeAppLanguageUseCase,
    private val getLocaleLanguageAppUseCase: GetLocaleLanguageAppUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val currentLanguage = getAppLanguageUseCase()
            appLanguageRepository.setLanguageCode(currentLanguage)
        }
    }
    val languageCode: StateFlow<String> = appLanguageRepository
        .languageCode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = appLanguageRepository.getLocale()
        )


    fun switchLanguage(newLanguageCode: String) {
        viewModelScope.launch {
            changeAppLanguageUseCase(newLanguageCode)
        }
    }

    fun getLanguageLocale() : String {
        return getLocaleLanguageAppUseCase()
    }
}