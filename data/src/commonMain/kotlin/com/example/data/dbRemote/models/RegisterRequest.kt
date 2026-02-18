package com.example.data.dbRemote.models

import kotlinx.serialization.Serializable


@Serializable
data class RegisterRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Int,
    val goal: String,
    val trainingLevel: String,
    val email: String,
)


@Serializable
data class RegisterResponse(
    val token: String
)


