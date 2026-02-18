package com.example.domain.usecase.authentication.registerPages

import com.example.domain.model.ValidationResult
import com.example.domain.model.authentication.register.Goal
import com.example.domain.usecase.authentication.validations.ValidateGoals

class ValidateGoalsPageUseCase(
    private val validateGoals: ValidateGoals,
) {
    fun execute(
        goal: Goal?,
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