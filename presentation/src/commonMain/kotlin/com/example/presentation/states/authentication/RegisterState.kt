package com.example.presentation.states.authentication

import com.example.domain.model.authentication.register.Goal
import com.example.domain.model.authentication.register.RegisterPage
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.presentation.components.snackbar.SnackBarData

data class RegisterState(
    val page: RegisterPage = RegisterPage.Profile,
    // Values
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val age: String = "",
    val height: Int = 170,
    val weight: Int = 70,
    val gender: String? = null,
    val goals: Goal? = null,
    val trainingLevel: TrainingLevel? = null,
    // List Errors
    val ageErrors: List<String> = emptyList(),
    val nameErrors: List<String> = emptyList(),
    val emailErrors: List<String> = emptyList(),
    val goalsErrors: List<String> = emptyList(),
    val genderErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    val trainingLevelErrors: List<String> = emptyList(),

    val showGoalsDialog: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val snackBarData: SnackBarData? = null,
)


