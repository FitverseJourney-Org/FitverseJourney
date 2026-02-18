package com.example.domain.usecase.authentication.validations

import com.example.domain.model.ValidationResult
import com.example.domain.usecase.authentication.ValidationRegisterScreen

class ValidateGender {
    fun execute(value: String?): ValidationResult {
        val errors = mutableListOf<String>()

        if(!ValidationRegisterScreen.hasSelectedGender(value)){
            errors.add("Gender cannot be empty")
        }

        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}