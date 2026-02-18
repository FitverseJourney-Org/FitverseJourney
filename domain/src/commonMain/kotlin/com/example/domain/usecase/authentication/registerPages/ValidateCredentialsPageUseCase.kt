package com.example.domain.usecase.authentication.registerPages

import com.example.domain.model.ValidationResult
import com.example.domain.usecase.authentication.validations.ValidateEmail
import com.example.domain.usecase.authentication.validations.ValidatePassword

class ValidateCredentialsPageUseCase(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
){
    fun execute(
        email: String?,
        password: String?,
    ): Map<String, List<String>> {

        val errors = mutableMapOf<String, List<String>>()

        validateEmail.execute(email).also {
            if (it is ValidationResult.Invalid){
                errors["email"] = it.errors
            }
        }
        validatePassword.execute(password).also {
            if (it is ValidationResult.Invalid){
                errors["password"] = it.errors
            }
        }
        return emptyMap()
    }
}