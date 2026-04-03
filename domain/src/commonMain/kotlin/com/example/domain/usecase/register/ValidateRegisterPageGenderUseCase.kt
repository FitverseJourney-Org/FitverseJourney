package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.register.RegisterPageGenderValidationType

class ValidateRegisterPageGenderUseCase() {
    operator fun invoke(
        registerGender: RegisterGender
    ) : RegisterPageGenderValidationType {
        if (registerGender == RegisterGender.NONE) {
            return RegisterPageGenderValidationType.EmptyField
        }
        return RegisterPageGenderValidationType.Valid
    }
}