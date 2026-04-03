package com.example.domain.model.register

enum class RegisterPageCredentialsValidationType {
    EmptyFields,
    NoEmail,
    NoPassword,
    WeakPassword,
    InvalidEmail,
    Valid
}