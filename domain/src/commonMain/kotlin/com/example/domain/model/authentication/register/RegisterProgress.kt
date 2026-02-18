package com.example.domain.model.authentication.register

fun RegisterPage.progress(): Float =
    when (this) {
        RegisterPage.Profile -> 0.00f
        RegisterPage.Goals -> 0.25f
        RegisterPage.Level -> 0.50f
        RegisterPage.Credentials -> 0.75f
        else -> 1f
    }