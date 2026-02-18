package com.example.presentation.presenter

import com.example.domain.model.dbLocal.language.Language
import com.example.domain.model.dbLocal.language.TagLanguage
import com.example.domain.usecase.datastore.LanguageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppPresenter(
    private val languageUseCase: LanguageUseCase,
    externalScope: CoroutineScope? = null
) {
    // scope interno apenas se externalScope == null
    private val ownScope = externalScope == null
    private val scope: CoroutineScope = externalScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _languageState = MutableStateFlow(TagLanguage.entries.first()) // fallback até receber valor real
    val languageState: StateFlow<TagLanguage> = _languageState

    private val _isChangingLanguage = MutableStateFlow(false)
    val isChangingLanguage: StateFlow<Boolean> = _isChangingLanguage

    init {
        // inicializa com leitura pontual (se possível) e depois observa atualizações
        scope.launch {
            // tenta obter valor inicial de forma segura; se falhar, mantém fallback
            runCatching {
                languageUseCase.getLanguage()
            }.getOrNull()?.let { initial ->
                _languageState.value = initial
            }

            // observa mudanças subsequentes de forma reativa
            languageUseCase.observeLanguage().collectLatest { tag ->
                _languageState.value = tag
            }
        }
    }

    /**
     * Realiza troca do idioma e mostra indicador.
     * É uma função suspensa: chame via coroutine (ex.: lifecycleScope.launch { ... }).
     */
    suspend fun switchLanguageAndShow(target: TagLanguage) {
        _isChangingLanguage.value = true
        try {
            // escreve no datastore via use case
            languageUseCase.setLanguage(target)

            // opcional: aguarda um curto tempo para animação / transição
            delay(600L)

            // opcional: aguardar explicitamente até que o fluxo emita o novo idioma
            // languageUseCase.observeLanguage().first { it == target } // se quiser bloquear até a confirmação
        } catch (t: Throwable) {
            // trate/logue o erro conforme seu logger; não propague por padrão
            // e.g., Timber.e(t, "Failed to change language")
        } finally {
            _isChangingLanguage.value = false
        }
    }

    /**
     * Conveniência assíncrona para trocar idioma sem bloquear o chamador.
     */
    fun switchLanguage(language: TagLanguage) {
        scope.launch {
            try {
                languageUseCase.setLanguage(language)
            } catch (_: Throwable) {
                // opcional: log
            }
        }
    }

    // leitura pontual (se precisar forçar)
    fun refreshLanguage() {
        scope.launch {
            runCatching {
                languageUseCase.getLanguage()
            }.onSuccess { current ->
                _languageState.value = current
            }
        }
    }

    // cancelar scope se for criado internamente
    fun clear() {
        if (ownScope) {
            scope.cancel()
        }
    }
}