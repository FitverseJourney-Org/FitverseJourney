package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterGoal
import com.example.domain.model.register.RegisterPageGoalsValidationType

class ValidateRegisterPageGoalsUseCase() {
    operator fun invoke(
        registerGoal: RegisterGoal
    ) : RegisterPageGoalsValidationType {
        if (registerGoal == RegisterGoal.NONE) {
            return RegisterPageGoalsValidationType.EmptyField
        }
        return RegisterPageGoalsValidationType.Valid
    }
}