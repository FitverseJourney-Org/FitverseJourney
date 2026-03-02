package com.example.presentation.screens.ui.authentication.login

import androidx.lifecycle.ViewModel
import com.example.domain.model.authentication.login.LoginAction
import com.example.domain.model.dbLocal.language.Language
import com.example.domain.model.dbLocal.language.TagLanguage
import com.example.domain.usecase.authentication.login.LoginUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.core.utils.LanguageAvailableApp
import com.example.presentation.states.authentication.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _uiAction = MutableSharedFlow<LoginAction>(replay = 0)
    val uiAction: SharedFlow<LoginAction> = _uiAction






    fun onAction(action: LoginAction){
        when(action){
            is LoginAction.EmailChanged -> {
                _state.update {
                    it.copy(email = action.value)
                }
            }
            is LoginAction.PasswordChanged -> {
                _state.update {
                    it.copy(password = action.value)
                }
            }
            is LoginAction.LanguageChanged -> {
                // atualiza estado e persiste escolha
                _state.update {
                    it.copy(language = action.value)
                }

            }
            is LoginAction.TogglePasswordVisibility -> {
                onTogglePasswordVisibility()
            }
            is LoginAction.Login -> {
                onLoginClick()
            }
        }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        scope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val result = loginUseCase(
                _state.value.email,
                _state.value.password
            )

            _state.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    },
                    onFailure = { error ->
                        state.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = error.message
                                    ?: "Something went wrong. Please try again.",
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                )
            }
        }
    }


    private fun mapTagToLanguage(tag: TagLanguage): Language =
        LanguageAvailableApp.availableLanguages.firstOrNull { it.code == tag } ?: LanguageAvailableApp.availableLanguages.first()

    private fun mapLanguageToTag(language: Language): TagLanguage =
        language.code


    fun consumeSnackBar() {
        _state.update {
            it.copy(snackBarData = null)
        }
    }
}