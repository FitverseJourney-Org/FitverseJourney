package com.example.domain.usecase.authentication.registerPages

import com.example.domain.model.ValidationResult
import com.example.domain.usecase.authentication.authValidations.ValidateAge
import com.example.domain.usecase.authentication.authValidations.ValidateGender
import com.example.domain.usecase.authentication.authValidations.ValidateName

class ValidateProfilePageUseCase(
    private val validateName: ValidateName,
    private val validateAge: ValidateAge,
    private val validateGender: ValidateGender,
) {
    fun execute(
        name: String?,
        gender: String?,
        age: Int?,
    ): Map<String, List<String>> {

        val errors = mutableMapOf<String, List<String>>()

        validateName.execute(name).also {
            if (it is ValidationResult.Invalid){
                errors["name"] = it.errors
            }
        }
        validateAge.execute(age).also {
            if (it is ValidationResult.Invalid){
                errors["age"] = it.errors
            }
        }
        validateGender.execute(gender).also {
            if (it is ValidationResult.Invalid){
                errors["gender"] = it.errors
            }
        }
        return errors
    }
}