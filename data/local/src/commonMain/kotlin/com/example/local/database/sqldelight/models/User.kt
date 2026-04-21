package com.example.local.database.sqldelight.models

data class User(
    val id: Long, // ou Int, String (UUID), dependendo do seu banco
    val name: String,
    val email: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val gender: String, // pode ser um Enum também
    val experienceLevel: String, // pode ser um Enum
    val goals: String,
    val targetWeight: Double,
    val targetCalories: Int,
    val targetProtein: Double,
    val targetCarbs: Double,
    val targetFat: Double,
    val updatedAt: Long // ou java.time.LocalDateTime, String, dependendo de como você lida com datas
)
