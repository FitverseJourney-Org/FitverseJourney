package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.authentication.register.RegisterGoal
import com.example.domain.repository.authentication.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        registerGender: RegisterGender,
        age: Int,
        height: Int,
        weight: Double,
        experienceLevel: RegisterExperienceLevel,
        goal: RegisterGoal
    ): Result<Unit> {
        authRepository.register(
            email = email,
            password = password,
        )
        return Result.success(Unit)
    }
}