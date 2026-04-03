package com.example.domain.usecase.register

import com.example.domain.model.register.RegisterPageCredentialsValidationType
import com.example.domain.utils.isEmptyInput
import com.example.domain.utils.isValidEmail
import com.example.domain.utils.isValidPassword

class ValidateRegisterPageCredentialsUseCase() {
    operator fun invoke(
        email: String,
        password: String
    ) : RegisterPageCredentialsValidationType {
        if (email.isBlank() && password.isBlank()) {
            return RegisterPageCredentialsValidationType.EmptyFields
        }
        if (email.isEmptyInput()) {
            return RegisterPageCredentialsValidationType.EmptyEmail
        }
        if (!email.isValidEmail()) {
            return RegisterPageCredentialsValidationType.InvalidEmail
        }
        if (password.isEmptyInput()) {
            return RegisterPageCredentialsValidationType.EmptyPassword
        }
        if (!password.isValidPassword()) {
            return RegisterPageCredentialsValidationType.WeakPassword
        }
        return RegisterPageCredentialsValidationType.Valid
    }
}