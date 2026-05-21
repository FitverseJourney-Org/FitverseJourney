package org.fitverse.data.remote.datasource.user

import org.fitverse.data.remote.dto.user.UserRequestDto
import org.fitverse.data.remote.util.ApiConstants
import org.fitverse.data.remote.util.ApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Implementação com Ktor Client
 * Responsável por todas as chamadas HTTP à API
 */
class UserRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : UserRemoteDataSource {

    override suspend fun getUserById(userId: String): UserRequestDto {
        return httpClient.get {
            url("${ApiConstants.BASE_URL}/users/$userId")
        }.body<ApiResponse<UserRequestDto>>().data
    }

    override suspend fun createUser(user: UserRequestDto) {
        httpClient.post {
            url("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.USERS}")
            contentType(ContentType.Application.Json)
            setBody(user)
        }.also { response ->
            if (!response.status.isSuccess()) {
                error("Erro ao criar usuário: ${response.status}")
            }
        }
    }

    override suspend fun updateUser(userId: String, user: UserRequestDto): UserRequestDto {
        return httpClient.put {
            url("${ApiConstants.BASE_URL}/users/$userId")
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<ApiResponse<UserRequestDto>>().data
    }

    override suspend fun deleteUser(userId: String) {
        httpClient.delete {
            url("${ApiConstants.BASE_URL}/users/$userId")
        }
    }
}