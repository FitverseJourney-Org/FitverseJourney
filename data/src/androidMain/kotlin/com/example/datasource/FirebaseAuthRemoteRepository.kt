package com.example.datasource

import android.util.Log
import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.SignUp
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRemoteRepository(
    private val auth: FirebaseAuth
) : AuthRemoteRepository {
    override suspend fun login(email: String,password: String): Result<UserToken> {
        return runCatching {

            val result = auth.signInWithEmailAndPassword(email, password).await()
                .user?.getIdToken(true)
                ?.addOnCompleteListener {  task ->
                    if(task.isSuccessful){
                        Log.d("CODE", task.result.token ?: "")
                    }else{
                        Log.w("CODE", "signInWithEmail:failure", task.exception)
                    }
                }?.await()
            UserToken(result?.token)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return runCatching {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d("TAGGG", "Email sent.")
                    }else{
                        Log.w("TAGGG", "Email failed.")
                    }
                }
        }
    }

    override suspend fun register(data: SignUp): Result<Unit> =
        runCatching {
            // cria o usuário e já autentica
            val authResult = auth.createUserWithEmailAndPassword(data.email, data.password).await()
            val user = authResult.user ?: throw IllegalStateException("User is null after registration")
            // atualiza o perfil do usuário
            Unit
        }.also { result ->
            result.exceptionOrNull()?.let { Log.w("TAGGG", "createUserWithEmail:failure", it) }
        }

}
