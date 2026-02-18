package com.example.domain.usecase.authentication

import com.example.domain.model.authentication.register.Goal
import com.example.domain.model.authentication.register.RegisterRequest
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.domain.repository.authentication.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nickname: String,
        email: String,
        password: String,
        gender: String,
        age: Int,
        height: Int,
        trainingLevel: TrainingLevel,
        weight: Int,
        goal: Goal
    ): Result<Unit> {
        return authRepository.register(
            data = RegisterRequest(
                name = nickname,
                email = email,
                password = password,
                gender = gender,
                age = age,
                height = height,
                goal = goal.code,
                weight = weight,
                trainingLevel = trainingLevel.name
            )
        )
    }
}