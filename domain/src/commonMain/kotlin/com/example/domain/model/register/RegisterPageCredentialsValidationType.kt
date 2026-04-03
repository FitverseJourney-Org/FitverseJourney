package com.example.domain.model.register

enum class RegisterPageCredentialsValidationType {
    EmptyFields,
    EmptyEmail,
    EmptyPassword,
    WeakPassword,
    InvalidEmail,
    Valid
}