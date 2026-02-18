package com.example.domain.usecase.authentication.validations

import com.example.domain.model.ValidationResult

class ValidateEmail {

    fun execute(value: String?): ValidationResult {
        val errors = mutableListOf<String>()

        if (value.isNullOrBlank()) {
            errors.add("O e-mail não pode estar vazio")
        } else {
            val emailRegex = Regex(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )

            if (!emailRegex.matches(value)) {
                errors.add("E-mail inválido")
            }
        }

        return if (errors.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(errors)
        }
    }
}
