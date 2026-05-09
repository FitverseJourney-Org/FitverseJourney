package com.example.presentation.ui.authentication.register.states

import com.example.domain.models.user.ClassType
import com.example.domain.models.user.Genero
import com.example.domain.models.user.NivelExperiencia
import com.example.domain.models.user.Objetivo

sealed interface RegisterIntent {
    // Step 1
    data class NomeChanged(val value: String) : RegisterIntent
    data class LastnameChanged(val value: String) : RegisterIntent
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data object TogglePasswordVisibility : RegisterIntent

    // Step 2
    data class UsernameChanged(val value: String) : RegisterIntent
    data class BirthDateChanged(val value: String) : RegisterIntent
    data class WeightChanged(val value: String) : RegisterIntent
    data class HeightChanged(val value: String) : RegisterIntent
    data class GeneroSelected(val genero: Genero) : RegisterIntent
    data class WeightUnitChanged(val unit: WeightUnit) : RegisterIntent
    data class HeightUnitChanged(val unit: HeightUnit) : RegisterIntent

    // Step 3
    data class ClassSelected(val classes: ClassType) : RegisterIntent
    data object ConfirmClass : RegisterIntent

    // Step 4
    data class ObjectiveToggled(val objective: Objetivo) : RegisterIntent
    data class LevelSelected(val level: NivelExperiencia) : RegisterIntent

    data object Submit : RegisterIntent

    // Navigation
    data object Next : RegisterIntent
    data object Back : RegisterIntent
    data object Leave : RegisterIntent
}