package com.example.presentation.ui.authentication.register.states

import com.example.domain.models.user.ClassType
import com.example.domain.models.user.Genero
import com.example.domain.models.user.NivelExperiencia
import com.example.domain.models.user.Objetivo
import com.example.domain.models.validations.ValidationType
import com.example.presentation.ui.authentication.register.states.snackbar.SnackbarEvent

data class RegisterUiState(
    val currentStep: RegisterStep = RegisterStep.CONTA,
    val snackbarEvent: SnackbarEvent? = null,
    // Step 1 – Conta
    val nome: String = "",
    val lastname: String = "",
    val email: String = "",
    val senha: String = "",
    val senhaVisible: Boolean = false,
    // Step 2 – Perfil
    val username: String = "",
    val birthDate: String = "",
    val weightUnit: WeightUnit = WeightUnit.KG,
    val heightUnit: HeightUnit = HeightUnit.CM,
    val birthDatePattern: String = "DD/MM/YYYY", // ← padrão estático, atualizado pela UI
    val weight: String = "",
    val height: String = "",
    // Step 3 – Classe
    val selectedClass: ClassType? = null,
    // Step 4 – Objetivos
    val selectedObjetivos: Set<Objetivo> = emptySet(),
    val nivelExperiencia: NivelExperiencia? = null,
    val genero: Genero = Genero.MASCULINO,
    // Validation
    val nomeError: ValidationType? = null,
    val lastnameError: ValidationType? = null,
    val emailError: ValidationType? = null,
    val senhaError: ValidationType? = null,
    val usernameError: ValidationType? = null,
    val birthDateError: ValidationType? = null,
    val weightError: ValidationType? = null,
    val heightError: ValidationType? = null,
    val generoError: ValidationType? = null,

    // Loading / submission
    val isLoading: Boolean = false,
    val registrationComplete: Boolean = false,
    val registrationCancellable: Boolean = false,
    val globalError: String? = null,
)