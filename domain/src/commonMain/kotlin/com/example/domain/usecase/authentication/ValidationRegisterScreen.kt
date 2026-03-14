package com.example.domain.usecase.authentication

import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.TrainingLevel

object ValidationRegisterScreen {
    fun hasMinimumLength(password: String, min: Int = 6): Boolean =
        password.length >= min

    fun hasNumber(password: String): Boolean =
        password.any { it.isDigit() }

    fun hasMaximumLength(password: String, max: Int = 10): Boolean =
        password.length <= max

    fun hasSelectedGoals(goal: FitnessGoal?): Boolean {
        return goal != null
    }
    fun hasSelectedLevel(level: TrainingLevel?): Boolean {
        return level != null
    }
    fun hasEmailValid(email: String): Boolean {
        return true
    }
    fun hasSelectedGender(gender: String?): Boolean {
        return gender != null
    }

    fun hasMinimumAge(age: Int, min: Int = 18): Boolean {
        return age >= min
    }

}