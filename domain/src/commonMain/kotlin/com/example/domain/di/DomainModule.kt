package com.example.domain.di


import com.example.domain.usecase.authentication.login.LoginUseCase
import com.example.domain.usecase.authentication.register.RegisterUseCase
import com.example.domain.usecase.authentication.reset.ResetPasswordUseCase
import org.koin.dsl.module

val domainModule = module {
    factory{
        LoginUseCase(
            authRepository = get()
        )
    }
    factory {
        ResetPasswordUseCase(
            authRepository = get()
        )
    }
    factory {
        RegisterUseCase(
            authRepository = get()
        )
    }
}