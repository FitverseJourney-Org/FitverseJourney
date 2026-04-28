package com.example.domain.models.auth.register

import com.example.domain.models.local.ClassType
import com.example.domain.models.local.Genero
import com.example.domain.models.local.NivelExperiencia
import com.example.domain.models.local.Objetivo
import com.example.domain.models.auth.register.snackbar.SnackbarEvent
import com.example.domain.models.validations.ValidationError

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
    val nomeError: ValidationError? = null,
    val lastnameError: ValidationError? = null,
    val emailError: ValidationError? = null,
    val senhaError: ValidationError? = null,
    val usernameError: ValidationError? = null,
    val birthDateError: ValidationError? = null,
    val weightError: ValidationError? = null,
    val heightError: ValidationError? = null,
    val generoError: ValidationError? = null,

    // Loading / submission
    val isLoading: Boolean = false,
    val registrationComplete: Boolean = false,
    val registrationCancellable: Boolean = false,
    val globalError: String? = null,
)