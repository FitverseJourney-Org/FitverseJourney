package com.example.domain.model.authentication.register

enum class RegisterPage {
    Profile,
    Metrics,
    Gender,
    Goals,
    Level,
    Avatar,
    Macros, // Nova etapa adicionada antes das credenciais
    Credentials,
    Success;

    fun next(): RegisterPage {
        return when (this) {
            Profile     -> Metrics
            Metrics     -> Gender
            Gender      -> Goals
            Goals       -> Level
            Level       -> Avatar
            Avatar      -> Macros
            Macros      -> Credentials
            Credentials -> Success
            Success     -> Success // Evita exceção se já estiver no final
        }
    }

    fun previous(): RegisterPage {
        return when (this) {
            Success     -> Credentials
            Credentials -> Macros      // Voltar das credenciais leva para os Macros
            Macros      -> Avatar      // Voltar dos Macros leva para o Avatar
            Avatar      -> Level
            Level       -> Goals
            Goals       -> Gender
            Gender      -> Metrics
            Metrics     -> Profile
            Profile     -> Profile // Evita exceção se já estiver no início
        }
    }
}