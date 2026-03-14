package com.example.domain.usecase.authentication.registerPages

import com.example.domain.model.ValidationResult
import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.usecase.authentication.authValidations.ValidateGoals

class ValidateGoalsPageUseCase(
    private val validateGoals: ValidateGoals,
) {
    fun execute(
        goal: FitnessGoal?,
    ): List<String> {
        val errors = mutableListOf<String>()


        validateGoals.execute(goal).also {
            if (it is ValidationResult.Invalid){
                errors.addAll(it.errors)
            }
        }
        return emptyList()

    }

}