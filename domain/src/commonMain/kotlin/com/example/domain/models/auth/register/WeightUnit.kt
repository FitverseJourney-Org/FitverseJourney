package com.example.domain.models.auth.register

enum class WeightUnit(val label: String) {
    KG("kg"), LBS("lbs");

    fun convert(value: Double, to: WeightUnit): Double = when {
        this == KG  && to == LBS -> value * 2.20462
        this == LBS && to == KG  -> value * 0.453592
        else -> value
    }
}