package com.example.presentation.screens.ui.authentication.register.state

import com.example.domain.model.authentication.register.RegisterAvatar
import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.authentication.register.RegisterGoal
import com.example.domain.model.authentication.register.RegisterMacros
import com.example.domain.model.authentication.register.RegisterPage
import com.example.presentation.components.snackbar.SnackBarData

data class RegisterState(
    val page: RegisterPage = RegisterPage.Profile,

    // 1° Page
    val firstName: String = "",
    val lastName: String = "",

    // 2° Page
    val height: Int = 170,
    val age: String = "",
    val weight: Int = 70,

    // 3° Page
    val registerGender: RegisterGender = RegisterGender.NONE,

    // 4° Page
    val registerGoal: RegisterGoal = RegisterGoal.NONE,

    // 5° Page
    val registerExperienceLevel: RegisterExperienceLevel = RegisterExperienceLevel.NONE,
    // 6° Page
    val selectedAvatar: RegisterAvatar = RegisterAvatar.NONE,

    // 7° Page
    val macroGoals: RegisterMacros = RegisterMacros(),
    val targetCalories: String = "",
    val targetProteins: String = "",
    val targetFats: String = "",
    val targetWater: String = "",
    val targetCarbs: String = "",

    // 8° Page
    val email: String = "",
    val password: String = "",
    val passwordShown: Boolean = false,
    // Validation Errors
    val ageErrors: List<String> = emptyList(),
    val nameErrors: List<String> = emptyList(),
    val emailErrors: List<String> = emptyList(),
    val goalsErrors: List<String> = emptyList(),
    val genderErrors: List<String> = emptyList(),
    val passwordErrors: List<String> = emptyList(),
    val experienceLevelErrors: List<String> = emptyList(),

    // Dialogs
    val dialogStatusAvatar: Boolean = false,
    // Loading
    val isLoading: Boolean = false,
    val snackBarData: SnackBarData? = null,

    val errorMessageInput : String? = null,
    val errorMessageLoginProcess : String? = null,
    val isSuccessfullyRegistered : Boolean = false
)