package com.example.remote.datasource.user

import com.example.domain.model.user.dto.ApiResponse
import com.example.data.model.dto.user.UserDto
import com.example.domain.model.user.dto.UserRequestDto
import com.example.remote.util.ApiConstants
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

    override suspend fun getUserById(userId: String): UserDto {
        return httpClient.get {
            url("${ApiConstants.BASE_URL}/users/$userId")
        }.body<ApiResponse<UserDto>>().data
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return httpClient.get {
            url("${ApiConstants.BASE_URL}/users")
        }.body<ApiResponse<List<UserDto>>>().data
    }

    override suspend fun createUser(user: UserRequestDto): UserDto {
        return httpClient.post {
            url("${ApiConstants.BASE_URL}/users")
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<ApiResponse<UserDto>>().data
    }

    override suspend fun updateUser(userId: String, user: UserRequestDto): UserDto {
        return httpClient.put {
            url("${ApiConstants.BASE_URL}/users/$userId")
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<ApiResponse<UserDto>>().data
    }

    override suspend fun deleteUser(userId: String) {
        httpClient.delete {
            url("${ApiConstants.BASE_URL}/users/$userId")
        }
    }

    override suspend fun getUserByEmail(email: String): UserDto {
        return httpClient.get {
            url("${ApiConstants.BASE_URL}/users/email/$email")
        }.body<ApiResponse<UserDto>>().data
    }

}