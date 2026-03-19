package com.example.presentation.screens.ui.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.RegisterPage
import com.example.domain.usecase.authentication.authValidations.ValidateAge
import com.example.domain.usecase.authentication.authValidations.ValidateEmail
import com.example.domain.usecase.authentication.authValidations.ValidateGender
import com.example.domain.usecase.authentication.authValidations.ValidateGoals
import com.example.domain.usecase.authentication.authValidations.ValidateName
import com.example.domain.usecase.authentication.authValidations.ValidatePassword
import com.example.domain.usecase.authentication.authValidations.ValidateTrainingLevel
import com.example.domain.usecase.authentication.register.RegisterUseCase
import com.example.domain.usecase.authentication.registerPages.ValidateCredentialsPageUseCase
import com.example.domain.usecase.authentication.registerPages.ValidateGoalsPageUseCase
import com.example.domain.usecase.authentication.registerPages.ValidateProfilePageUseCase
import com.example.domain.usecase.authentication.registerPages.ValidateTrainingLevelUseCase
import com.example.presentation.components.snackbar.SnackBarData
import com.example.presentation.components.snackbar.SnackbarType
import com.example.presentation.navigationState.RegisterNavigation
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
            is RegisterAction.NameChanged -> {
                _state.update { it.copy(name = action.value, nameErrors = emptyList()) }
            }
            is RegisterAction.EmailChanged -> {
                _state.update { it.copy(email = action.value, emailErrors = emptyList()) }
            }
            is RegisterAction.PasswordChanged -> {
                _state.update { it.copy(password = action.value, passwordErrors = emptyList()) }
            }
            is RegisterAction.AgeChanged -> {
                _state.update { it.copy(age = action.value, ageErrors = emptyList()) }
            }
            is RegisterAction.HeightChanged -> {
                _state.update { it.copy(height = action.value) }
            }
            is RegisterAction.GenderChanged -> {
                _state.update { it.copy(gender = action.value, genderErrors = emptyList()) }
            }
            is RegisterAction.WeightChanged -> {
                _state.update { it.copy(weight = action.value) }
            }
            is RegisterAction.GoalsChanged -> {
                _state.update { it.copy(fitnessGoal = action.value) }
            }
            is RegisterAction.TrainingLevelChanged -> {
                _state.update {
                    it.copy(
                        trainingLevel = it.trainingLevel,
                        trainingLevelErrors = emptyList()
                    )
                }
            }

            is RegisterAction.Next -> {
                validateAndNext()
            }
            is RegisterAction.GoalsClean -> {
                _state.update { it.copy(goalsErrors = emptyList()) }
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
        }
    }
    private fun validateAndNext() {
        when (_state.value.page) {
            RegisterPage.Profile -> validateProfile()
            RegisterPage.Goals -> validateGoals()
            RegisterPage.Level -> validateLevel()
            RegisterPage.Credentials -> validateCredentials()
            else -> nextPage()
        }
    }
    private fun validateProfile() {
        val errors = ValidateProfilePageUseCase(
            validateName = ValidateName(),
            validateAge = ValidateAge(),
            validateGender = ValidateGender(),
        ).execute(
            name = _state.value.name,
            gender = _state.value.gender!!.name,
            age = _state.value.age.toIntOrNull(),
        )

        if (errors.isEmpty()) {
            nextPage()
        } else {
            _state.update {
                it.copy(
                    nameErrors = errors["name"] ?: emptyList(),
                    ageErrors = errors["age"] ?: emptyList(),
                    genderErrors = errors["gender"] ?: emptyList()
                )
            }
        }
    }
    private fun validateGoals() {
        val errors = ValidateGoalsPageUseCase(
            validateGoals = ValidateGoals()
        ).execute(goal = _state.value.fitnessGoal)

        if(errors.isEmpty()){
            nextPage()
        } else {
            _state.update {
                it.copy(
                    goalsErrors = errors,
                    snackBarData = SnackBarData(
                        message = "Please select a goal",
                        type = SnackbarType.ERROR
                    )
                )
            }
        }
    }
    private fun validateLevel() {
        val errors = ValidateTrainingLevelUseCase(
            validateLevel = ValidateTrainingLevel()
        ).execute(fitnessLevel = _state.value.trainingLevel)

        if(errors.isEmpty()){
            nextPage()
        } else {
            _state.update {
                it.copy(
                    trainingLevelErrors = errors,
                    snackBarData = SnackBarData(
                        message = "Please select a your training level",
                        type = SnackbarType.ERROR
                    )
                )
            }
        }
    }
    private fun validateCredentials() {
        val errors = ValidateCredentialsPageUseCase(
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword()
        ).execute(
            email = _state.value.email,
            password = _state.value.password
        )

        if (errors.isEmpty()) {
            nextPage()
        } else {
            _state.update {
                it.copy(
                    emailErrors = errors["email"] ?: emptyList(),
                    passwordErrors = errors["password"] ?: emptyList(),
                )
            }
        }
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
                nickname = _state.value.name,
                email = _state.value.email,
                age = _state.value.age.toInt(),
                height = _state.value.height,
                gender = _state.value.gender,
                trainingLevel = _state.value.trainingLevel,
                goal = _state.value.fitnessGoal!!,
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