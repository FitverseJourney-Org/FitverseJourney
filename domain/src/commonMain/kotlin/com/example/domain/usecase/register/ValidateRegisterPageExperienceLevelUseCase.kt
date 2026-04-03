package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.register.RegisterPageExperienceLevelValidationType

class ValidateRegisterPageExperienceLevelUseCase() {
    operator fun invoke(
        registerExperienceLevel: RegisterExperienceLevel
    ) : RegisterPageExperienceLevelValidationType {
        if (registerExperienceLevel == RegisterExperienceLevel.NONE) {
            return RegisterPageExperienceLevelValidationType.EmptyField
        }
        return RegisterPageExperienceLevelValidationType.Valid
    }
}