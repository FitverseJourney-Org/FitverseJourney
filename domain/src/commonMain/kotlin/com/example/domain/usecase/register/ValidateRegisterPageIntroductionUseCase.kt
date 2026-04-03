package com.example.domain.usecase.register

import com.example.domain.model.register.RegisterPageIntroductionValidationType

class ValidateRegisterPageIntroductionUseCase() {
    operator fun invoke(
        firstName: String,
        lastName: String
    ) : RegisterPageIntroductionValidationType {
        if (firstName.isBlank() && lastName.isBlank()) {
            return RegisterPageIntroductionValidationType.EmptyFields
        }
        if(firstName.isBlank()) {
            return RegisterPageIntroductionValidationType.NoFirstName
        }
        if(lastName.isBlank()) {
            return RegisterPageIntroductionValidationType.NoLastName
        }
        if (firstName.length < 3) {
            return RegisterPageIntroductionValidationType.MinLengthFirstName
        }
        if (lastName.length < 3) {
            return RegisterPageIntroductionValidationType.MinLengthLastName
        }
        return RegisterPageIntroductionValidationType.Valid
    }
}