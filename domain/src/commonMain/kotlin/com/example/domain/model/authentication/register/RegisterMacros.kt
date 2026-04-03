package com.example.domain.model.authentication.register

data class RegisterMacros(
    val calories: Int = 0,
    val carbohydrates: Int = 0,
    val proteins: Int = 0,
    val fats: Int = 0,
    val waterMl: Int = 0
){
    // Helper para formatar valores caso precise exibir em algum lugar
    fun getFormattedWater(): String = "${waterMl / 1000.0}L"
}
