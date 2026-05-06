package com.example.presentation.ui.authentication.register

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.expect.AgeCalculator
import com.example.domain.models.local.User
import com.example.domain.usecase.register.RegisterUseCase
import com.example.expect.NumberFormatter
import com.example.expect.TimerManager
import com.example.domain.models.validations.ValidationType
import com.example.presentation.ui.authentication.register.states.HeightUnit
import com.example.presentation.ui.authentication.register.states.RegisterIntent
import com.example.presentation.ui.authentication.register.states.RegisterStep
import com.example.presentation.ui.authentication.register.states.RegisterUiState
import com.example.presentation.ui.authentication.register.states.WeightUnit
import com.example.presentation.ui.authentication.register.states.snackbar.SnackbarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            // ── Conta ──────────────────────────────────────────────────────
            is RegisterIntent.NomeChanged -> _uiState.update { it.copy(nome = intent.value, nomeError = null) }
            is RegisterIntent.LastnameChanged -> _uiState.update { it.copy(lastname = intent.value, lastnameError = null) }
            is RegisterIntent.EmailChanged -> _uiState.update { it.copy(email = intent.value.trim(), emailError = null) }
            is RegisterIntent.PasswordChanged -> _uiState.update {it.copy(senha = intent.value.trim(),senhaError = null)}
            is RegisterIntent.TogglePasswordVisibility -> _uiState.update { it.copy(senhaVisible = !it.senhaVisible) }


            // ── Perfil ─────────────────────────────────────────────────────
            is RegisterIntent.UsernameChanged -> _uiState.update { it.copy(username = intent.value, usernameError = null) }
            is RegisterIntent.HeightChanged -> _uiState.update { it.copy(height = intent.value, heightError = null) }
            is RegisterIntent.WeightChanged -> _uiState.update { it.copy(weight = intent.value, weightError = null) }
            is RegisterIntent.BirthDateChanged -> formatterBirthDate(intent.value)
            is RegisterIntent.WeightUnitChanged -> {
                val current   = _uiState.value
                val rawValue  = current.weight.toDoubleOrNull() ?: 0.0
                val converted = current.weightUnit.convert(rawValue, intent.unit)
                val formatted = if (converted == 0.0) "" else NumberFormatter.formatOneDecimal(converted) // ✅

                _uiState.update {
                    it.copy(
                        weightUnit = intent.unit,
                        weight     = formatted
                    )
                }
            }

            is RegisterIntent.HeightUnitChanged -> {
                val current   = _uiState.value
                val rawValue  = current.height.toDoubleOrNull() ?: 0.0
                val converted = current.heightUnit.convert(rawValue, intent.unit)
                val formatted = if (converted == 0.0) "" else NumberFormatter.formatOneDecimal(converted) // ✅

                _uiState.update {
                    it.copy(
                        heightUnit = intent.unit,
                        height     = formatted
                    )
                }
            }
            is RegisterIntent.ConfirmClass -> advanceStep()


            // ── Classe ─────────────────────────────────────────────────────
            is RegisterIntent.ClassSelected -> _uiState.update { it.copy(selectedClass = intent.classes) }


            // ── Objetivos ──────────────────────────────────────────────────
            is RegisterIntent.ObjectiveToggled -> _uiState.update { state ->
                val updated = state.selectedObjetivos.toMutableSet().also { set ->
                    if (intent.objective in set) set.remove(intent.objective)
                    else set.add(intent.objective)
                }
                state.copy(selectedObjetivos = updated)
            }
            is RegisterIntent.LevelSelected -> _uiState.update { it.copy(nivelExperiencia = intent.level) }
            is RegisterIntent.GeneroSelected -> _uiState.update { it.copy(genero = intent.genero) }


            is RegisterIntent.Submit -> submitRegistration()


            // ── Navigation ─────────────────────────────────────────────────
            is RegisterIntent.Next -> if (validateCurrentStep()) advanceStep()
            is RegisterIntent.Back -> _uiState.update { state ->
                val prevIndex = (state.currentStep.index - 1).coerceAtLeast(0)
                state.copy(currentStep = RegisterStep.Companion.fromIndex(prevIndex))
            }
            is RegisterIntent.Leave -> _uiState.update { it.copy(registrationCancellable = true) }

        }
    }

    private fun validateCurrentStep(): Boolean {
        val state = _uiState.value
        return when (state.currentStep) {
            RegisterStep.CONTA     -> validateAccount(state)
            RegisterStep.PERFIL    -> validateProfile(state)
            RegisterStep.CLASSE    -> validateClass(state)
            RegisterStep.OBJETIVOS -> validateObjectives(state)
        }
    }


    private fun validateAccount(state: RegisterUiState): Boolean {
        val nomeError     = when {
            state.nome.isBlank()       -> ValidationType.BLANK
            state.nome.length < 2      -> ValidationType.TOO_SHORT
            else                       -> null
        }
        val lastnameError = when {
            state.lastname.isBlank()   -> ValidationType.BLANK
            state.lastname.length < 2  -> ValidationType.TOO_SHORT
            else                       -> null
        }
        val emailError = when {
            state.email.isBlank()      -> ValidationType.BLANK
            !state.email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                -> ValidationType.INVALID_FORMAT
            else                       -> null
        }
        val passwordError = when {
            state.senha.length < 8              -> ValidationType.TOO_SHORT
            !state.senha.contains(Regex("[0-9]")) -> ValidationType.INVALID_FORMAT
            else                                -> null
        }

        _uiState.update {
            it.copy(
                nomeError     = nomeError,
                lastnameError = lastnameError,
                emailError    = emailError,
                senhaError    = passwordError,
            )
        }

        return listOf(nomeError, lastnameError, emailError, passwordError).all { it == null }
    }

    private fun validateProfile(state: RegisterUiState): Boolean {
        val usernameError = when {
            state.username.isBlank()                            -> ValidationType.BLANK
            state.username.length < 3                           -> ValidationType.TOO_SHORT
            state.username.length > 20                          -> ValidationType.TOO_LONG
            !state.username.matches(Regex("^[a-zA-Z0-9._]+$")) -> ValidationType.INVALID_FORMAT
            else                                                -> null
        }

        val birthDateError = when {
            state.birthDate.isBlank()        -> ValidationType.BLANK
            state.birthDate.length < 8       -> ValidationType.TOO_SHORT
            !isValidDate(state.birthDate)    -> ValidationType.INVALID_DATE
            !isValidAge(state.birthDate)     -> ValidationType.INVALID_AGE
            else                             -> null
        }

        val weight = state.weight.toDoubleOrNull()
        val (weightMin, weightMax) = when (state.weightUnit) {
            WeightUnit.KG  -> 20.0 to 300.0
            WeightUnit.LBS -> 44.0 to 661.0
        }
        val weightError = when {
            state.weight.isBlank() -> ValidationType.BLANK
            weight == null         -> ValidationType.INVALID_FORMAT
            weight !in weightMin..weightMax -> ValidationType.OUT_OF_RANGE
            else                   -> null
        }

        val height = state.height.toDoubleOrNull()
        val (heightMin, heightMax) = when (state.heightUnit) {
            HeightUnit.CM -> 100.0 to 250.0
            HeightUnit.FT -> 3.0   to 8.2
        }
        val heightError = when {
            state.height.isBlank() -> ValidationType.BLANK
            height == null         -> ValidationType.INVALID_FORMAT
            height !in heightMin..heightMax -> ValidationType.OUT_OF_RANGE
            else                   -> null
        }
        val genderError = when {
            state.genero == null -> ValidationType.REQUIRED
            else                 -> null
        }

        _uiState.update {
            it.copy(
                usernameError  = usernameError,
                birthDateError = birthDateError,
                weightError    = weightError,
                heightError    = heightError,
                generoError    = genderError,
            )
        }

        return listOf(usernameError, birthDateError, weightError, heightError).all { it == null }
    }

    private fun validateClass(state: RegisterUiState): Boolean {
        // Sem ValidationError pois o botão já fica disabled se nada selecionado
        return state.selectedClass != null
    }

    private fun validateObjectives(state: RegisterUiState): Boolean {
        // Objetivos e nível são opcionais — submit é sempre liberado
        return true
    }

    private fun advanceStep() {
        _uiState.update { state ->
            val nextIndex =
                (state.currentStep.index + 1).coerceAtMost(RegisterStep.entries.last().index)
            state.copy(currentStep = RegisterStep.Companion.fromIndex(nextIndex))
        }
    }
    fun onLocaleResolved(pattern: String) {
        _uiState.update { it.copy(birthDatePattern = pattern) }
    }

    fun formatterBirthDate(value: String) {
        val digitsOnly = value.filter { it.isDigit() }.take(8)
        _uiState.update { it.copy(birthDate = digitsOnly, birthDateError = null) }
    }


    private fun isValidDate(digits: String): Boolean {
        if (digits.length < 8) return false
        val pattern = _uiState.value.birthDatePattern
        return try {
            val (d, m, y) = when (pattern) {
                "YYYY/MM/DD" -> Triple(
                    digits.substring(6, 8).toInt(),
                    digits.substring(4, 6).toInt(),
                    digits.take(4).toInt()
                )
                "MM/DD/YYYY" -> Triple(
                    digits.substring(2, 4).toInt(),
                    digits.take(2).toInt(),
                    digits.substring(4, 8).toInt()
                )
                else -> Triple(                        // DD/MM/YYYY (padrão BR)
                    digits.take(2).toInt(),
                    digits.substring(2, 4).toInt(),
                    digits.substring(4, 8).toInt()
                )
            }
            m in 1..12 && d in 1..31 && y > 1900
        } catch (e: Exception) { false }
    }

    private fun isValidAge(digits: String): Boolean {
        val pattern = _uiState.value.birthDatePattern
        val formatted = when (pattern) {
            "YYYY/MM/DD" -> "${digits.substring(6, 8)}/${digits.substring(4, 6)}/${digits.take(4)}"
            "MM/DD/YYYY" -> "${digits.substring(2, 4)}/${digits.take(2)}/${digits.substring(4, 8)}"
            else         -> "${digits.take(2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
        }
        val age = AgeCalculator.fromBirthDate(formatted)
        return age in 13..100
    }


    private fun submitRegistration() {
        val state = _uiState.value
        val now = TimerManager.nowMillis()

        val userData = User(
            name = state.nome,
            lastname = state.lastname,
            email = state.email,
            username = state.username,
            birthDate = state.birthDate,
            weight = state.weight.toDouble(),
            height = state.height.toInt(),
            gender = state.genero,
            classType = state.selectedClass!!,
            goals = state.selectedObjetivos.joinToString(", ") { it.name },
            experienceLevel = state.nivelExperiencia!!.name,
            updatedAt = now,
            createdAt = now,
            isPremium = false,
        )

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val response = registerUseCase(
                email = state.email,
                password = state.senha,
                userData = userData
            )

            if (response.isSuccess) {
                _uiState.update {
                    it.copy(
                        registrationComplete = true,
                        isLoading            = false,
                        snackbarEvent        = SnackbarEvent(
                            message = "Conta criada com sucesso! 🎉",
                            isError = false,
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading     = false,
                        snackbarEvent = SnackbarEvent(
                            message = response.exceptionOrNull()?.message ?: "Erro ao criar conta",
                            isError = true,
                        )
                    )
                }
            }
        }
    }
    // Chame após a UI consumir o evento
    fun onSnackbarConsumed() {
        _uiState.update { it.copy(snackbarEvent = null) }
    }
}

class DateVisualTransformation(private val pattern: String = "DD/MM/YYYY"):VisualTransformation{

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text

        val out = buildString {
            trimmed.forEachIndexed { index, char ->
                append(char)
                if (pattern == "YYYY/MM/DD") {
                    if (index == 3 || index == 5) append("/")
                } else {
                    if (index == 1 || index == 3) append("/")
                }
            }
        }

        return TransformedText(
            text = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when (pattern) {
                        "YYYY/MM/DD" -> when {
                            offset <= 3 -> offset
                            offset <= 5 -> offset + 1
                            offset <= 8 -> offset + 2
                            else -> 10
                        }
                        else -> when { // DD/MM/YYYY e MM/DD/YYYY
                            offset <= 1 -> offset
                            offset <= 3 -> offset + 1
                            offset <= 8 -> offset + 2
                            else -> 10
                        }
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when (pattern) {
                        "YYYY/MM/DD" -> when {
                            offset <= 4 -> offset
                            offset <= 6 -> offset - 1
                            offset <= 10 -> offset - 2
                            else -> 8
                        }
                        else -> when {
                            offset <= 2 -> offset
                            offset <= 5 -> offset - 1
                            offset <= 10 -> offset - 2
                            else -> 8
                        }
                    }
                }
            }
        )
    }
}
class WeightVisualTransformation(private val unit: WeightUnit):VisualTransformation{

    override fun filter(text: AnnotatedString): TransformedText {
        val suffix = unit.label                    // "kg" ou "lbs"
        val out    = text.text + suffix            // "60kg" / "132lbs"

        return TransformedText(
            text          = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = offset
                override fun transformedToOriginal(offset: Int): Int =
                    offset.coerceAtMost(text.text.length)
            }
        )
    }
}
class HeightVisualTransformation(private val unit: HeightUnit):VisualTransformation{

    override fun filter(text: AnnotatedString): TransformedText {
        val suffix = unit.label                    // "cm" ou "ft"
        val out    = text.text + suffix            // "180cm" / "5.11ft"

        return TransformedText(
            text          = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int = offset
                override fun transformedToOriginal(offset: Int): Int =
                    offset.coerceAtMost(text.text.length)
            }
        )
    }
}