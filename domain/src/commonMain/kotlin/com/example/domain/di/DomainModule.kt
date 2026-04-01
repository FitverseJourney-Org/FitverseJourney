package com.example.domain.di


import com.example.domain.usecase.database.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.database.datastore.authentication.SetIsAuthenticatedUseCase
import com.example.domain.usecase.database.datastore.language.ObserveAppLanguageUseCase
import com.example.domain.usecase.database.datastore.language.SetAppLanguageUseCase
import com.example.domain.usecase.database.datastore.onboarding.ObserveOnboardingCompletedUseCase
import com.example.domain.usecase.database.datastore.onboarding.SetOnboardingCompletedUseCase
import com.example.domain.usecase.login.LoginUseCase
import com.example.domain.usecase.register.RegisterUseCase
import com.example.domain.usecase.reset.ResetPasswordUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
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

    // Modules dataStore
    factory { ObserveIsAuthenticatedUseCase(repository = get()) }
    factory { ObserveOnboardingCompletedUseCase(repository = get()) }
    factory { ObserveAppLanguageUseCase(repository = get()) }
    factory { SetIsAuthenticatedUseCase(repository = get()) }
    factory { SetOnboardingCompletedUseCase(repository = get()) }
    factory { SetAppLanguageUseCase(repository = get()) }
}