package com.example.datasource

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterUser
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthDataSource(
    private val auth : FirebaseAuth
) : AuthRemoteRepository {
    override suspend fun login(email: String, password: String): Result<UserToken> {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).result
            val user = authResult.user ?: throw IllegalStateException("User is null after login")

            // Obtém o token de autenticação do usuário.
            val tokenResult = user.getIdToken(true)
            val userToken = UserToken(tokenResult.result.token)
            return Result.success(userToken)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
            try {
                auth.sendPasswordResetEmail(email)
                return Result.success(Unit)
            } catch (e: Exception) {
                return Result.success(Unit)
            }
    }

    override suspend fun register(data: RegisterUser): Result<Unit> {
        try {
            val authResult = auth.createUserWithEmailAndPassword(data.email, data.password).result

            // Obtém o token de autenticação do usuário.
            val user = authResult.user ?: throw IllegalStateException("User is null after login")
            val tokenResult = user.getIdToken(true)
            val userToken = UserToken(tokenResult.result.token)


            //

            return Result.success(Unit)
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

}
