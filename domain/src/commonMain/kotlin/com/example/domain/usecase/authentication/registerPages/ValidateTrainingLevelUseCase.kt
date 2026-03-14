package com.example.domain.usecase.authentication.registerPages

import com.example.domain.model.ValidationResult
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.domain.usecase.authentication.authValidations.ValidateTrainingLevel

class ValidateTrainingLevelUseCase(
    private val validateLevel: ValidateTrainingLevel
) {
    fun execute(
        fitnessLevel: FitnessLevel?,
    ): List<String> {
        val errors = mutableListOf<String>()
        validateLevel.execute(fitnessLevel).also {
            if (it is ValidationResult.Invalid){
                errors.addAll(it.errors)
            }
        }
        return errors
    }
}