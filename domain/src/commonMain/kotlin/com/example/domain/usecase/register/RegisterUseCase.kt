package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.RegisterExperienceLevel
import com.example.domain.model.authentication.register.RegisterGender
import com.example.domain.model.authentication.register.RegisterGoal
import com.example.domain.model.authentication.register.RegisterUser
import com.example.domain.repository.authentication.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nickname: String,
        email: String,
        password: String,
        registerGender: RegisterGender,
        age: Int,
        height: Int,
        trainingLevel: RegisterExperienceLevel,
        weight: Int,
        registerGoal: RegisterGoal
    ): Result<Unit> {
        return authRepository.register(
            data = RegisterUser(
                name = nickname,
                email = email,
                password = password,
                registerGender = registerGender,
                birthDate = age.toString(),
                heightCm = height,
                weightKg = weight.toDouble(),
                registerExperienceLevel = trainingLevel,
                registerGoal = registerGoal
            )
        )
    }
}