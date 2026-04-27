package com.example.remote.datasource.user

import ApiResponse
import com.example.remote.dto.user.UserDto
import com.example.remote.dto.user.UserRequestDto
import com.example.remote.util.ApiConstants
import com.example.remote.util.ApiResponse
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

    override suspend fun createUser(user: UserRequestDto): UserRequestDto {
        return httpClient.post {
            url("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.USERS}")
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<ApiResponse<UserRequestDto>>().data
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