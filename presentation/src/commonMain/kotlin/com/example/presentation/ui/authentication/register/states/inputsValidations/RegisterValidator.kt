package com.example.presentation.ui.authentication.register.states.inputsValidations

import com.example.domain.models.validations.ValidationType
import com.example.domain.models.validations.ValidationResult

sealed class RegisterValidator<T> {

    abstract fun validate(value: T): ValidationResult

    data object Name : RegisterValidator<String>() {
        override fun validate(value: String) = when {
            value.isBlank() -> ValidationResult.Invalid(ValidationType.BLANK)
            value.length < 2 -> ValidationResult.Invalid(ValidationType.TOO_SHORT)
            else -> ValidationResult.Valid
        }
    }

    data object Email : RegisterValidator<String>() {
        private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        override fun validate(value: String) = when {
            value.isBlank() -> ValidationResult.Invalid(ValidationType.BLANK)
            !emailRegex.matches(value) -> ValidationResult.Invalid(ValidationType.INVALID_FORMAT)
            else -> ValidationResult.Valid
        }
    }

    data object Weight : RegisterValidator<Double>() {
        override fun validate(value: Double) = when {
            value !in 30.0..300.0 -> ValidationResult.Invalid(ValidationType.OUT_OF_RANGE)
            else -> ValidationResult.Valid
        }
    }
}