package com.example.domain.usecase.authentication.authValidations

import com.example.domain.model.ValidationResult

class ValidateHeight {
    fun execute(): ValidationResult {
        val errors = mutableListOf<String>()


        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}