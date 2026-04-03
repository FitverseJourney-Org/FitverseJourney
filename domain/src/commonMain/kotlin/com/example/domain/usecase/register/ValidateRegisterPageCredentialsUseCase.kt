package com.example.domain.usecase.register

import com.example.domain.model.register.RegisterPageCredentialsValidationType
import com.example.domain.model.register.RegisterPageIntroductionValidationType

class ValidateRegisterPageCredentialsUseCase() {
    operator fun invoke(
        email: String,
        password: String
    ) : RegisterPageCredentialsValidationType {
        if (email.isBlank() && password.isBlank()) {
            return RegisterPageCredentialsValidationType.EmptyFields
        }

        if(email.isBlank()) {
            return RegisterPageCredentialsValidationType.NoEmail
        }
        if(!email.contains("@")) {
            return RegisterPageCredentialsValidationType.InvalidEmail
        }

        if(password.isBlank()) {
            return RegisterPageCredentialsValidationType.NoPassword
        }

        if(password.length < 8) {
            return RegisterPageCredentialsValidationType.WeakPassword
        }


        return RegisterPageCredentialsValidationType.Valid
    }
}