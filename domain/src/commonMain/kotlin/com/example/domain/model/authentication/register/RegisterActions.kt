package com.example.domain.model.authentication.register

sealed class RegisterAction {
    data class NameChanged(val value: String) : RegisterAction()
    data class EmailChanged(val value: String) : RegisterAction()
    data class PasswordChanged(val value: String) : RegisterAction()
    data class AgeChanged(val value: String) : RegisterAction()
    data class HeightChanged(val value: Int) : RegisterAction()
    data class WeightChanged(val value: Int) : RegisterAction()
    data class GenderChanged(val value: String) : RegisterAction()
    data class GoalsChanged(val value: Goal) : RegisterAction()
    data class TrainingLevelChanged(val level: TrainingLevel) : RegisterAction()

    object GoalsClean : RegisterAction()

    object Next : RegisterAction()
    object Exit : RegisterAction()
    object Back : RegisterAction()
    object Finish : RegisterAction()
    object Submit : RegisterAction()
}
