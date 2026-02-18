package com.example.domain.usecase.authentication.validations

import com.example.domain.model.ValidationResult
import com.example.domain.model.authentication.register.TrainingLevel

class ValidateTrainingLevel{

    fun execute(level: TrainingLevel?): ValidationResult {
        return if (level == null) {
            ValidationResult.Invalid(listOf("Please select your training level"))
        } else {
            ValidationResult.Valid
        }
    }
}