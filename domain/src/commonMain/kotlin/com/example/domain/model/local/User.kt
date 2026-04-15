package com.example.domain.model.local

data class User(
    val id: Long?,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int, // Calculado a partir da birthDate
    val weight: Double,
    val height: Int,
    val experienceLevel: String,
    val goals: String,
    val isPremium: Boolean,
    // Metas de Saúde
    val targetWeight: Double,
    val targetCalories: Int,
    val targetProtein: Double,
    val targetCarbs: Double,
    val targetFat: Double,
    val createdAt: Long,
    val updatedAt: Long
)