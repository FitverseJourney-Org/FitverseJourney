package com.example.domain.usecase.authentication.validations

import com.example.domain.model.ValidationResult
import com.example.domain.usecase.authentication.ValidationRegisterScreen


class ValidateAge {
    fun execute(age: Int?): ValidationResult {
        val errors = mutableListOf<String>()

        if(age == null){
            errors.add("Age cannot be empty")
        }else{
            if(!ValidationRegisterScreen.hasMinimumAge(age)){
                errors.add("Age must be greater than 18")
            }
        }


        return if (errors.isEmpty())
            ValidationResult.Valid
        else
            ValidationResult.Invalid(errors)
    }
}