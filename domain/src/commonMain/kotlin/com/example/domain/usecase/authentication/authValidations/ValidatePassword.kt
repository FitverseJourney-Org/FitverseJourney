package com.example.domain.usecase.authentication.authValidations

import com.example.domain.model.ValidationResult
import com.example.domain.usecase.authentication.ValidationRegisterScreen.hasMinimumLength
import com.example.domain.usecase.authentication.ValidationRegisterScreen.hasNumber

class ValidatePassword {
    fun execute(value: String?): ValidationResult {
        val errors = mutableListOf<String>()

        if(value.isNullOrBlank()) {
            errors.add("Password cannot be empty")
        }else{
            if (!hasMinimumLength(password = value, min = 6)) {
                errors.add("Password must have at least 6 characters")
            }
            if (!hasNumber(value)) {
                errors.add("Password must have at least 1 number")
            }
        }

        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}