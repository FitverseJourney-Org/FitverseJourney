package com.example.domain.model.authentication.register

enum class RegisterPage {
    Profile,
    Goals,
    Level,
    Credentials,
    Success;

    fun next(): RegisterPage {
        return when (this) {
            Profile -> Goals
            Goals -> Level
            Level -> Credentials
            Credentials -> Success
            Success -> Success
        }
    }

    fun previous(): RegisterPage {
        return when (this) {
            Success -> Credentials
            Credentials -> Level
            Level  -> Goals
            Goals -> Profile
            Profile -> Profile
        }
    }
}