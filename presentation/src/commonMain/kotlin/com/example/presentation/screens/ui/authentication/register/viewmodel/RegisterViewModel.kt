package com.example.presentation.screens.ui.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.authentication.register.RegisterMacros
import com.example.domain.model.authentication.register.RegisterPage
import com.example.domain.model.register.RegisterPageAvatarValidationType
import com.example.domain.model.register.RegisterPageCredentialsValidationType
import com.example.domain.model.register.RegisterPageExperienceLevelValidationType
import com.example.domain.model.register.RegisterPageGenderValidationType
import com.example.domain.model.register.RegisterPageGoalsValidationType
import com.example.domain.model.register.RegisterPageIntroductionValidationType
import com.example.domain.model.register.RegisterPageMacrosValidationType
import com.example.domain.usecase.register.RegisterUseCase
import com.example.domain.usecase.register.ValidateRegisterPageAvatarUseCase
import com.example.domain.usecase.register.ValidateRegisterPageCredentialsUseCase
import com.example.domain.usecase.register.ValidateRegisterPageExperienceLevelUseCase
import com.example.domain.usecase.register.ValidateRegisterPageGenderUseCase
import com.example.domain.usecase.register.ValidateRegisterPageGoalsUseCase
import com.example.domain.usecase.register.ValidateRegisterPageIntroductionUseCase
import com.example.domain.usecase.register.ValidateRegisterPageMacrosUseCase
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
    private val registerUseCase: RegisterUseCase,
    private val validateRegisterPageIntroductionUseCase: ValidateRegisterPageIntroductionUseCase,
    private val validateRegisterPageGenderUseCase: ValidateRegisterPageGenderUseCase,
    private val validateRegisterPageGoalsUseCase: ValidateRegisterPageGoalsUseCase,
    private val validateRegisterPageExperienceLevelUseCase: ValidateRegisterPageExperienceLevelUseCase,
    private val validateRegisterPageAvatarUseCase: ValidateRegisterPageAvatarUseCase,
    private val validateRegisterPageMacrosUseCase: ValidateRegisterPageMacrosUseCase,
    private val validateRegisterPageCredentialsUseCase: ValidateRegisterPageCredentialsUseCase
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
                _state.update { it.copy(firstName = action.value) }
            }
            is RegisterAction.LastName -> {
                _state.update { it.copy(lastName = action.value) }
            }

            // 2° Page Actions
            is RegisterAction.HeightChanged -> {
                _state.update { it.copy(height = action.value) }
            }
            is RegisterAction.DateOfBirthChanged -> {
                _state.update { it.copy(age = action.year.toString()) }
            }
            is RegisterAction.WeightChanged -> {
                _state.update { it.copy(weight = action.value) }
            }

            // 3° Page Actions
            is RegisterAction.GenderChanged -> {
                _state.update { it.copy(registerGender = action.value) }
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
                        experienceLevelErrors = emptyList()
                    )
                }
            }

            // 6° Page Actions
            is RegisterAction.AvatarChanged -> {
                _state.update {
                    it.copy(selectedAvatar = action.avatar) // Use o novo nome aqui
                }
            }

            // 7° Page Actions
            is RegisterAction.UpdateCalories -> {
                _state.update { it.copy(
                    targetCalories = action.value,
                    macroGoals = it.macroGoals.copy(calories = action.value.toIntOrNull() ?: 0)
                ) }
            }
            is RegisterAction.UpdateProteins -> {
                _state.update { it.copy(
                    targetProteins = action.value,
                    macroGoals = it.macroGoals.copy(proteins = action.value.toIntOrNull() ?: 0)
                ) }
            }
            is RegisterAction.UpdateFats -> {
                _state.update {
                    it.copy(
                        targetFats = action.value,
                        macroGoals = it.macroGoals.copy(fats = action.value.toIntOrNull() ?: 0)
                    )
                }
            }
            is RegisterAction.UpdateCarbs -> {
                _state.update {
                    it.copy(
                        targetCarbs = action.value,
                        macroGoals = it.macroGoals.copy(carbohydrates = action.value.toIntOrNull() ?: 0)
                    )
                }
            }
            is RegisterAction.UpdateWater -> {
                _state.update {
                    it.copy(
                        targetWater = action.value,
                        macroGoals = it.macroGoals.copy(waterMl = action.value.toIntOrNull() ?: 0)
                    )
                }
            }
            is RegisterAction.UpdateMacros -> {
                _state.update { it.copy(macroGoals = action.macros) }
            }
            RegisterAction.AutoAdjustMacros -> {
                processDataCalcMacros()
            }

            // 8° Page Actions
            is RegisterAction.EmailChanged -> {
                _state.update { it.copy(email = action.value) }
            }
            is RegisterAction.PasswordChanged -> {
                _state.update { it.copy(password = action.value) }
            }
            is RegisterAction.PasswordShown -> {
                _state.update { it.copy(passwordShown = !action.value) }
            }


            // -------------------------------
            // --- ACTIONS BUTTONS --------
            // -------------------------------

            is RegisterAction.Next -> {
                validateAndNext()
            }
            is RegisterAction.Back -> {
                goPrevious()
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




        }
    }
    private fun validateAndNext() {
        val currentState = _state.value

        when (currentState.page) {
            RegisterPage.Profile -> {
                // Supondo que seu UseCase receba esses parâmetros. Ajuste conforme sua assinatura real.
                val result = validateRegisterPageIntroductionUseCase(
                    firstName = currentState.firstName,
                    lastName = currentState.lastName, // Adicione outros se necessário (age, height, weight)
                )
                when (result) {
                    RegisterPageIntroductionValidationType.Valid -> nextPage()
                    RegisterPageIntroductionValidationType.NoFirstName -> showError("O nome não pode estar vazio.")
                    RegisterPageIntroductionValidationType.NoLastName -> showError("O sobrenome não pode estar vazio.")
                    // Adicione outros casos do seu Enum de Introduction aqui
                    else -> showError("Verifique os dados informados.")
                }
            }

            RegisterPage.Gender -> {
                val result = validateRegisterPageGenderUseCase(currentState.registerGender)
                when (result) {
                    RegisterPageGenderValidationType.Valid -> nextPage()
                    RegisterPageGenderValidationType.EmptyField -> showError("Por favor, selecione um gênero.")
                }
            }

            RegisterPage.Goals -> {
                val result = validateRegisterPageGoalsUseCase(currentState.registerGoal)
                when (result) {
                    RegisterPageGoalsValidationType.Valid -> nextPage()
                    RegisterPageGoalsValidationType.EmptyField -> showError("Por favor, escolha seu objetivo principal no Fitverse.")
                }
            }

            RegisterPage.ExperienceLevel -> {
                val result = validateRegisterPageExperienceLevelUseCase(currentState.registerExperienceLevel)
                when (result) {
                    RegisterPageExperienceLevelValidationType.Valid -> nextPage()
                    RegisterPageExperienceLevelValidationType.EmptyField -> showError("Qual o seu nível de experiência?")
                }
            }

            RegisterPage.Avatar -> {
                // currentState.selectedAvatar agora é RegisterAvatar?
                // e o UseCase espera RegisterAvatar. Tudo certo!
                val result = validateRegisterPageAvatarUseCase(currentState.selectedAvatar)

                when (result) {
                    RegisterPageAvatarValidationType.Valid -> nextPage()
                    RegisterPageAvatarValidationType.EmptyField -> showError("Escolha um avatar para continuar.")
                }
            }

            RegisterPage.Macros -> {
                // Utilizando o UserMacros que consolidamos anteriormente
                val result = validateRegisterPageMacrosUseCase(currentState.macroGoals)
                when (result) {
                    RegisterPageMacrosValidationType.Valid -> nextPage()
                    RegisterPageMacrosValidationType.EmptyFields -> showError("Preencha suas metas de macros.")
                    RegisterPageMacrosValidationType.NoCalories -> showError("As calorias não podem ser zero.")
                    RegisterPageMacrosValidationType.NoCarbohydrates -> showError("Os carboidratos não podem ser zero.")
                    RegisterPageMacrosValidationType.NoProteins -> showError("As proteínas não podem ser zero.")
                    RegisterPageMacrosValidationType.NoFats -> showError("As gorduras não podem ser zero.")
                    RegisterPageMacrosValidationType.NoWater -> showError("A meta de água não pode ser zero.")
                }
            }

            RegisterPage.Credentials -> {
                val result = validateRegisterPageCredentialsUseCase(
                    email = currentState.email,
                    password = currentState.password
                )
                when (result) {
                    RegisterPageCredentialsValidationType.Valid -> {
                        // Se Credenciais for a última página, você pode chamar o submitRegistration()
                        // Ou se tiver uma página de resumo, chame nextPage()
                        submitRegistration()
                    }
                    RegisterPageCredentialsValidationType.EmptyEmail -> showError("O e-mail não pode estar vazio.")
                    RegisterPageCredentialsValidationType.InvalidEmail -> showError("Digite um e-mail válido.")
                    RegisterPageCredentialsValidationType.EmptyPassword -> showError("A senha não pode estar vazia.")
                    RegisterPageCredentialsValidationType.WeakPassword -> showError("Sua senha deve ter 8 caracteres, incluindo letras maiúsculas, minúsculas e números.")
                    RegisterPageCredentialsValidationType.EmptyFields -> showError("Preencha todos os campos.")
                }
            }
            RegisterPage.Metrics -> {
                nextPage()
            }
            RegisterPage.Success -> {
                // Não faz nada ou navega para o Login
            }
        }
    }
    private fun showError(message: String) {
        _state.update {
            it.copy(
                snackBarData = SnackBarData(
                    message = message,
                    type = SnackbarType.ERROR
                )
            )
        }
    }

    fun nextPage() {
        _state.update {
            it.copy(page = it.page.next(),snackBarData = null)
        }
    }
    private fun goPrevious() {
        _state.update { state ->
            state.copy(page = state.page.previous())
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
                registerGoal = _state.value.registerGoal,
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

    private fun processDataCalcMacros() {
        _state.update {
            it.copy(
                targetCalories = "2000",
                targetCarbs = "200",
                targetProteins = "100",
                targetFats = "80",
                targetWater = "8",
                macroGoals = RegisterMacros(
                    calories = 2000,
                    carbohydrates = 200,
                    proteins = 100,
                    fats = 80,
                    waterMl = 8
                )
            )
        }
    }
    fun consumeSnackBar() {
        _state.update {
            it.copy(snackBarData = null)
        }
    }
}