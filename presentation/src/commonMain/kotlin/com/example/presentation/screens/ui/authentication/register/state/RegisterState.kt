package com.example.presentation.screens.ui.authentication.register.state

import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.Gender
import com.example.domain.model.authentication.register.RegisterPage
import com.example.presentation.components.snackbar.SnackBarData

data class RegisterState(
    val page: RegisterPage = RegisterPage.Profile,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val age: String = "",
    val height: Int = 170,
    val weight: Int = 70,
    val selectedAvatarId: String? = null,
    val targetCalories: Int = 2000,
    val targetProteins: Int = 150,
    val targetFats: Int = 70,
    val targetWater: Int = 250,
    val targetCarbs: Int = 200,
    val gender: Gender = Gender.OTHER,
    val fitnessGoal: FitnessGoal? = null,
    val trainingLevel: FitnessLevel = FitnessLevel.BEGINNER,
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
    val dialogStatusAvatar: Boolean = false
)