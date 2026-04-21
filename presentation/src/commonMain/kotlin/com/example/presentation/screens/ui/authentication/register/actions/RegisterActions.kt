package com.example.presentation.screens.ui.authentication.register.actions

import com.example.domain.model.authentication.register.RegisterAvatar
import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.authentication.register.RegisterGoal
import com.example.domain.model.authentication.register.RegisterMacros

sealed class RegisterAction {

    // 1° Page
    data class FirstName(val value: String) : RegisterAction()
    data class LastName(val value: String) : RegisterAction()

    // 2° Page
    data class DateOfBirthChanged(val day: Int, val month: Int, val year: Int) : RegisterAction()
    data class HeightChanged(val value: Int) : RegisterAction()
    data class WeightChanged(val value: Double) : RegisterAction()

    // 3° Page
    data class GenderChanged(val value: RegisterGender) : RegisterAction()

    // 4° Page
    data class GoalChanged(val value: RegisterGoal) : RegisterAction()

    // 5° Page
    data class ExperienceLevelChanged(val level: RegisterExperienceLevel) : RegisterAction()

    // 6° Page
    data class AvatarChanged(val avatar: RegisterAvatar) : RegisterAction()

    // 7° Page
    data class UpdateMacros(val macros: RegisterMacros) : RegisterAction()
    data class UpdateCalories(val value: String) : RegisterAction()
    data class UpdateCarbs(val value: String) : RegisterAction()
    data class UpdateProteins(val value: String) : RegisterAction()
    data class UpdateFats(val value: String) : RegisterAction()
    data class UpdateWater(val value: String) : RegisterAction()
    object AutoAdjustMacros : RegisterAction()
    object ToggleEditingMacros : RegisterAction()



    // 8° Page
    data class EmailChanged(val value: String) : RegisterAction()
    data class PasswordChanged(val value: String) : RegisterAction()
    data class PasswordShown(val value: Boolean) : RegisterAction()


    // Dialogs
    data class DialogStatusAvatar(val value: Boolean) : RegisterAction()

    // Outros Actions Buttons
    object Next : RegisterAction()
    object Exit : RegisterAction()
    object Back : RegisterAction()
    object Finish : RegisterAction()
    object Submit : RegisterAction()
}