package com.example.local.model

data class MealEntryEntity(
    val id: String,
    val userId: String,
    val date: String,          // YYYY-MM-DD
    val mealType: String,      // BREAKFAST, LUNCH, DINNER, SNACK
    val name: String,
    val totalKcal: Int,
    val totalProtein: Double,  // gramas
    val totalCarbs: Double,
    val totalFat: Double,
    val loggedAt: Long,        // epoch ms
)
