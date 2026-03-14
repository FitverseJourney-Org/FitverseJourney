package com.example.domain.usecase.authentication.authValidations

import com.example.domain.model.ValidationResult
import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.usecase.authentication.ValidationRegisterScreen

class ValidateGoals {
    fun execute(value: FitnessGoal?): ValidationResult {
        val errors = mutableListOf<String>()

        if(!ValidationRegisterScreen.hasSelectedGender(value?.name)){
            errors.add("Gender cannot be empty")
        }

        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}