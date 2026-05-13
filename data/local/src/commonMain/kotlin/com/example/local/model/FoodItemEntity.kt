package com.example.local.model

data class FoodItemEntity(
    val id: String,
    val mealId: String,
    val name: String,
    val portion: Double,   // quantidade
    val unit: String,      // g, ml, unidade...
    val kcal: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)
