package org.fitverse.data.remote

import org.fitverse.domain.models.auth.AuthResult
import org.fitverse.domain.models.errors.AuthError
import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import org.fitverse.data.remote.datasource.auth.AuthRemoteDataSource
import org.fitverse.data.remote.dto.auth.AuthResultDto
import org.fitverse.data.remote.mapper.AuthMapper
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val authMapper: AuthMapper,
    private val appAuthenticateRepository: AppAuthenticateRepository,
) : AuthRepository, AuthRemoteDataSource {

    // ── Handlers centralizados ──────────────────────────────────────────────

    private fun handleFirebaseError(e: Exception): Nothing = throw when (e) {
        is CancellationException                  -> e  // sempre relançar
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials("Credenciais inválidas: ${e.message}")
        is FirebaseAuthInvalidUserException        -> AuthError.UserNotFound("Usuário não encontrado ou desabilitado: ${e.message}")
        is FirebaseAuthUserCollisionException      -> AuthError.EmailAlreadyInUse("E-mail já está em uso: ${e.message}")
        is FirebaseAuthWeakPasswordException       -> AuthError.WeakPassword("Senha fraca: ${e.reason}")
        is FirebaseAuthRecentLoginRequiredException-> AuthError.ReauthRequired("Reautenticação necessária: ${e.message}")
        is FirebaseAuthEmailException              -> AuthError.InvalidCredentials("Erro de e-mail: ${e.message}")
        is FirebaseAuthException                   -> AuthError.Unknown("Erro de autenticação [${e.errorCode}]: ${e.message}")
        is FirebaseNetworkException                -> AuthError.NetworkError("Sem conexão: verifique sua internet")
        is FirebaseTooManyRequestsException        -> AuthError.TooManyRequests("Muitas tentativas. Tente novamente mais tarde")
        is FirebaseApiNotAvailableException        -> AuthError.ServiceUnavailable("Serviço Firebase indisponível")
        else                                       -> AuthError.Unknown("Erro inesperado: ${e.message}")
    }

    private suspend fun FirebaseUser.getFreshToken(forceRefresh: Boolean = true): String =
        getIdToken(forceRefresh).await().token
            ?: throw AuthError.Unknown("Token inválido: Firebase retornou nulo")

    private fun mapToDto(user: FirebaseUser, token: String) = AuthResultDto(
        uid   = user.uid,
        email = user.email,
        token = token,
    )

    // ── Métodos públicos ────────────────────────────────────────────────────

    override suspend fun login(email: String, password: String): Result<AuthResult> = runCatching {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user   = result.user ?: throw AuthError.Unknown("Login falhou: usuário nulo")
        authMapper.mapDtoToDomain(mapToDto(user, user.getFreshToken()))
    }.recoverNetworkFallback(email).mapError(::handleFirebaseError)


    override suspend fun register(email: String, password: String): Result<AuthResult> = runCatching {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user   = result.user ?: throw AuthError.Unknown("Registro falhou: usuário nulo")
        authMapper.mapDtoToDomain(mapToDto(user, user.getFreshToken(forceRefresh = true)))
    }.mapError(::handleFirebaseError)


    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }


    override suspend fun logout() {
        appAuthenticateRepository.setIsAuthenticated(false)
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid
    // ── Extensões privadas ──────────────────────────────────────────────────

    /** Fallback offline: reutiliza sessão cacheada do Firebase sem chamar o servidor */
    private suspend fun Result<AuthResult>.recoverNetworkFallback(email: String): Result<AuthResult> =
        recover { error ->
            if (error !is FirebaseNetworkException) throw error
            val cached = firebaseAuth.currentUser
                ?.takeIf { it.email == email }
                ?: throw AuthError.NetworkError("Sem conexão e nenhuma sessão local encontrada")
            val token = runCatching { cached.getFreshToken(forceRefresh = false) }
                .getOrElse { throw AuthError.NetworkError("Sem conexão: sessão expirada") }
            authMapper.mapDtoToDomain(mapToDto(cached, token))
        }


    /** Converte qualquer exceção não-AuthError via handleFirebaseError */
    private fun <T> Result<T>.mapError(handler: (Exception) -> Nothing): Result<T> =
        onFailure { e ->
            if (e !is AuthError && e !is CancellationException)
                handler(e as Exception)
        }
}