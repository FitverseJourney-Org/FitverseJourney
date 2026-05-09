package com.example.domain.models.validations

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: ValidationType) : ValidationResult()
}