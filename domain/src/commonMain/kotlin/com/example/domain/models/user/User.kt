package com.example.domain.models.user

import com.example.domain.expect.AgeCalculator

data class User(
    val uid: String = "",
    val name: String,
    val email: String,
    val isPremium: Boolean = false,
    val lastname: String,
    val username: String,
    val birthDate: String,
    val weight: Double,
    val height: Int,
    val genero: Genero,
    val classType: ClassType,
    val goals: String,
    val experienceLevel: String,
    val targetWeight: Double? = null,
    val targetCalories: Int? = null,
    val targetProtein: Double?= null,
    val targetCarbs: Double? = null,
    val targetFat: Double? = null,
    val createdAt: Long,
    val updatedAt: Long,
){
    val age: Int get() = AgeCalculator.fromBirthDate(birthDate) // "25/04/1998" → 28
}