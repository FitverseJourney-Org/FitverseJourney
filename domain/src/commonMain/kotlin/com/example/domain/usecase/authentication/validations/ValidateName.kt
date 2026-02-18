package com.example.domain.usecase.authentication.validations

import com.example.domain.model.ValidationResult

class ValidateName {

    fun execute(value: String?): ValidationResult {
        val errors = mutableListOf<String>()

        if (value == null) {
            errors.add("Name cannot be empty")
        }else {
            if (value.isBlank()) {
                errors.add("Name cannot be empty")
            }

            if (value.length < 3) {
                errors.add("Name must have at least 3 characters")
            }
        }

        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}