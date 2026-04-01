package com.example.domain.usecase.register

import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.Gender
import com.example.domain.model.authentication.register.SignUp
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.domain.repository.authentication.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        nickname: String,
        email: String,
        password: String,
        gender: Gender,
        age: Int,
        height: Int,
        trainingLevel: FitnessLevel,
        weight: Int,
        goal: FitnessGoal
    ): Result<Unit> {
        return authRepository.register(
            data = SignUp(
                name = nickname,
                email = email,
                password = password,
                gender = gender,
                birthDate = age.toString(),
                heightCm = height,
                weightKg = weight.toDouble(),
                fitnessLevel = trainingLevel,
                fitnessGoal = goal
            )
        )
    }
}