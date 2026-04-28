package com.example.domain.models.validations

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: ValidationError) : ValidationResult()
}

enum class ValidationError {
    BLANK,
    TOO_SHORT,
    TOO_LONG,
    INVALID_FORMAT,
    INVALID_AGE,
    OUT_OF_RANGE,
    INVALID_DATE,
    REQUIRED,
}