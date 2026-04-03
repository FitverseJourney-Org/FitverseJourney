package com.example.presentation.screens.ui.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.authentication.register.RegisterPage
import com.example.domain.usecase.register.RegisterUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigationState.RegisterNavigation
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _navigationState = MutableSharedFlow<RegisterNavigation>()
    val navigationState = _navigationState.asSharedFlow()

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _uiEvent = MutableSharedFlow<RegisterAction>(replay = 0)
    val uiEvent: SharedFlow<RegisterAction> = _uiEvent

    fun onAction(action: RegisterAction) {
        when (action) {
            // 1° Page Actions
            is RegisterAction.FirstName -> {
                _state.update { it.copy(firstName = action.value, nameErrors = emptyList()) }
            }
            is RegisterAction.LastName -> {
                _state.update { it.copy(lastName = action.value, nameErrors = emptyList()) }
            }

            // 2° Page Actions
            is RegisterAction.HeightChanged -> {
                _state.update { it.copy(height = action.value) }
            }
            is RegisterAction.AgeChanged -> {
                _state.update { it.copy(age = action.value, ageErrors = emptyList()) }
            }
            is RegisterAction.WeightChanged -> {
                _state.update { it.copy(weight = action.value) }
            }

            // 3° Page Actions
            is RegisterAction.GenderChanged -> {
                _state.update { it.copy(registerGender = action.value, genderErrors = emptyList()) }
            }

            // 4° Page Actions
            is RegisterAction.GoalChanged -> {
                _state.update { it.copy(registerGoal = action.value) }
            }

            // 5° Page Actions
            is RegisterAction.ExperienceLevelChanged -> {
                _state.update {
                    it.copy(
                        registerExperienceLevel = action.level, // Corrigido para usar o valor da action
                        trainingLevelErrors = emptyList()
                    )
                }
            }

            // 6° Page Actions
            is RegisterAction.AvatarChanged -> {
                _state.update {
                    it.copy(selectedAvatarId = action.avatarId)
                }
            }

            // 7° Page Actions
            is RegisterAction.UpdateCalories -> {
                _state.update { it.copy(targetCalories = action.value) }
            }
            is RegisterAction.UpdateProteins -> {
                _state.update { it.copy(targetProteins = action.value) }
            }
            is RegisterAction.UpdateFats -> {
                _state.update { it.copy(targetFats = action.value) }
            }
            is RegisterAction.UpdateCarbs -> {
                _state.update { it.copy(targetCarbs = action.value) }
            }
            is RegisterAction.UpdateWater -> {
                _state.update { it.copy(targetWater = action.value) }
            }

            // 8° Page Actions
            is RegisterAction.EmailChanged -> {
                _state.update { it.copy(email = action.value, emailErrors = emptyList()) }
            }
            is RegisterAction.PasswordChanged -> {
                _state.update { it.copy(password = action.value, passwordErrors = emptyList()) }
            }

            // -------------------------------
            // --- ACTIONS BUTTONS --------
            // -------------------------------

            is RegisterAction.Next -> {
                validateAndNext()
            }
            is RegisterAction.Back -> {
                previousPageOrExit()
            }
            is RegisterAction.Submit -> {
                submitRegistration()
            }
            is RegisterAction.Exit -> {
                viewModelScope.launch {
                    _navigationState.emit(RegisterNavigation.ToLogin)
                }
            }
            is RegisterAction.Finish -> {
                viewModelScope.launch {
                    _navigationState.emit(RegisterNavigation.ToLogin)
                }
            }

            is RegisterAction.DialogStatusAvatar -> {
                _state.update {
                    it.copy(dialogStatusAvatar = action.value)
                }
            }

            is RegisterAction.UpdateMacros -> {
                _state.update { currentState ->
                    currentState.copy(
                        macroGoals = action.macros
                    )
                }
            }
        }
    }
    private fun validateAndNext() {
        nextPage()
    }

    private fun processValidations() {

    }
    fun nextPage() {
        _state.update {
            it.copy(
                page = it.page.next(),
                snackBarData = null
            )
        }
    }
    private fun previousPageOrExit() {
        _state.update { state ->
            if (state.page == RegisterPage.Profile) {
                state.copy(
                    snackBarData = SnackBarData(
                        message = "Registration cancelled",
                        type = SnackbarType.INFO
                    )
                )
            } else {
                state.copy(page = state.page.previous())
            }
        }
    }
    private fun submitRegistration() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = registerUseCase.invoke(
                nickname = _state.value.firstName,
                email = _state.value.email,
                age = _state.value.age.toInt(),
                height = _state.value.height,
                registerGender = _state.value.registerGender,
                trainingLevel = _state.value.registerExperienceLevel,
                registerGoal = _state.value.registerGoal!!,
                weight = _state.value.weight,
                password = _state.value.password
            )
            println(result)
            _state.update { state ->
                result.fold(
                    onSuccess = {
                        state.copy(
                            isLoading = false,
                            page = RegisterPage.Success
                        )
                    },
                    onFailure = { error ->
                        state.copy(
                            isLoading = false,
                            snackBarData = SnackBarData(
                                message = error.message ?: "Something went wrong",
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                )
            }
        }
    }
    fun consumeSnackBar() {
        _state.update {
            it.copy(snackBarData = null)
        }
    }
}