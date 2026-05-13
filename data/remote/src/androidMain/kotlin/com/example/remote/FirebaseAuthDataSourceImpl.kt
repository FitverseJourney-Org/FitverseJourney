package com.example.remote

import com.example.domain.models.auth.AuthResult
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthResultDto
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.remote.mapper.AuthMapper
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
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val authMapper: AuthMapper,
    private val appAuthenticateRepository: AppAuthenticateRepository,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user   = result.user ?: throw Exception("Login falhou: usuário nulo")
            val token = user.getIdToken(true).await().token  // true = força refresh
                ?: throw Exception("Token inválido: Firebase retornou token nulo")

            return authMapper.mapDtoToDomain(
                dto = AuthResultDto(
                    uid   = user.uid,
                    email = user.email,
                    token = token,
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            // Conta desativada ou não encontrada
            throw Exception("Usuário não encontrado ou conta desativada: ${e.message}")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // E-mail malformado ou senha incorreta
            throw Exception("Credenciais inválidas: ${e.message}")
        } catch (e: FirebaseAuthUserCollisionException) {
            // E-mail já vinculado a outro provedor
            throw Exception("Conflito de conta: ${e.message}")
        } catch (e: FirebaseAuthWeakPasswordException) {
            // Senha fraca (raro no login, mas possível em fluxos mistos)
            throw Exception("Senha fraca: ${e.message}")
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            // Operação sensível exige reautenticação recente
            throw Exception("Reautenticação necessária: ${e.message}")
        } catch (e: FirebaseAuthEmailException) {
            // Problema genérico relacionado ao e-mail
            throw Exception("Erro de e-mail: ${e.message}")
        } catch (e: FirebaseAuthException) {
            // Qualquer outro erro do FirebaseAuth não coberto acima
            throw Exception("Erro de autenticação [${e.errorCode}]: ${e.message}")
        } catch (e: FirebaseNetworkException) {
            // Sem conexão ou timeout de rede
            throw Exception("Erro de rede: verifique sua conexão")
        } catch (e: FirebaseTooManyRequestsException) {
            // Muitas tentativas → conta temporariamente bloqueada
            throw Exception("Muitas tentativas. Tente novamente mais tarde")
        } catch (e: FirebaseApiNotAvailableException) {
            // Google Play Services indisponível
            throw Exception("Serviço Firebase indisponível no momento")
        } catch (e: CancellationException) {
            // Coroutine cancelada — deve ser relançada sempre
            throw e
        } catch (e: Exception) {
            // Fallback para erros inesperados
            throw Exception("Erro inesperado: ${e.message}")
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user   = result.user ?: throw Exception("Registro falhou: usuário nulo")
            val token = user.getIdToken(false).await().token
                ?: throw Exception("Token inválido: Firebase retornou token nulo")

            return authMapper.mapDtoToDomain(
                AuthResultDto(
                    uid   = user.uid,
                    email = user.email,
                    token = token,
                )
            )
        } catch (e: FirebaseAuthWeakPasswordException) {
            // Senha não atende ao requisito mínimo do Firebase (6 caracteres)
            throw Exception("Senha fraca: ${e.reason}")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // E-mail com formato inválido
            throw Exception("E-mail inválido: ${e.message}")
        } catch (e: FirebaseAuthUserCollisionException) {
            // E-mail já cadastrado por este ou outro provedor
            throw Exception("E-mail já está em uso: ${e.message}")
        } catch (e: FirebaseAuthInvalidUserException) {
            // Conta criada mas imediatamente desabilitada por regra do projeto
            throw Exception("Usuário inválido ou desabilitado: ${e.message}")
        } catch (e: FirebaseAuthEmailException) {
            // Problema genérico relacionado ao e-mail
            throw Exception("Erro de e-mail: ${e.message}")
        } catch (e: FirebaseAuthException) {
            // Qualquer outro erro de autenticação não coberto acima
            throw Exception("Erro de autenticação [${e.errorCode}]: ${e.message}")
        } catch (e: FirebaseNetworkException) {
            // Sem conexão ou timeout durante o registro
            throw Exception("Erro de rede: verifique sua conexão")
        } catch (e: FirebaseTooManyRequestsException) {
            // Muitas requisições em curto período
            throw Exception("Muitas tentativas. Tente novamente mais tarde")
        } catch (e: FirebaseApiNotAvailableException) {
            // Google Play Services ausente ou desatualizado
            throw Exception("Serviço Firebase indisponível no momento")
        } catch (e: CancellationException) {
            // Cancelamento de coroutine — sempre relançar
            throw e
        } catch (e: Exception) {
            // Fallback para erros inesperados
            throw Exception("Erro inesperado: ${e.message}")
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            // Cancelamento de coroutine — sempre relançar
            throw e
        } catch (e: FirebaseAuthInvalidUserException) {
            // E-mail não cadastrado ou conta desabilitada
            Result.failure(Exception("Nenhuma conta encontrada para este e-mail: ${e.message}"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // E-mail com formato inválido
            Result.failure(Exception("E-mail inválido: ${e.message}"))
        } catch (e: FirebaseAuthEmailException) {
            // Problema genérico relacionado ao e-mail
            Result.failure(Exception("Erro de e-mail: ${e.message}"))
        } catch (e: FirebaseAuthException) {
            // Qualquer outro erro de auth não coberto acima
            Result.failure(Exception("Erro de autenticação [${e.errorCode}]: ${e.message}"))
        } catch (e: FirebaseNetworkException) {
            // Sem conexão ou timeout
            Result.failure(Exception("Erro de rede: verifique sua conexão"))
        } catch (e: FirebaseTooManyRequestsException) {
            // Muitos e-mails enviados em curto período
            Result.failure(Exception("Muitas tentativas. Tente novamente mais tarde"))
        } catch (e: FirebaseApiNotAvailableException) {
            // Google Play Services ausente ou desatualizado
            Result.failure(Exception("Serviço Firebase indisponível no momento"))
        } catch (e: Exception) {
            // Fallback para erros inesperados
            Result.failure(Exception("Erro inesperado: ${e.message}"))
        }
    }

    override suspend fun logout() {
        appAuthenticateRepository.setIsAuthenticated(false)
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid
}