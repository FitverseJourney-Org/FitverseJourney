package com.example.domain.model.register

enum class RegisterPageIntroductionValidationType {
    EmptyFields,
    NoFirstName,
    NoLastName,
    MinLengthFirstName,
    MinLengthLastName,
    Valid
}