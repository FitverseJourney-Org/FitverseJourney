package com.example.domain.usecase.login

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.repository.authentication.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserToken> {
        val result = authRepository.login(email, password)

        return if(result.isSuccess){
            Result.success(result.getOrThrow())
        }else{

            if(email.isEmpty() || password.isEmpty()) {
                Result.failure(Exception("Email or password cannot be empty"))
            }else{
                Result.failure(Exception("Invalid email or password"))
            }

        }
    }
}