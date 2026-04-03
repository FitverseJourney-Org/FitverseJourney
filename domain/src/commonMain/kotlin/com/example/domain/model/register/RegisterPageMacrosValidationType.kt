package com.example.domain.model.register

enum class RegisterPageMacrosValidationType {
    Valid,
    EmptyFields, // Usado quando TODOS forem 0
    NoCalories,
    NoCarbohydrates,
    NoProteins,
    NoFats,
    NoWater
}