package com.example.domain.di


import com.example.domain.usecase.database.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.database.datastore.authentication.SetIsAuthenticatedUseCase
import com.example.domain.usecase.database.datastore.language.ChangeAppLanguageUseCase
import com.example.domain.usecase.database.datastore.language.GetAppLanguageUseCase
import com.example.domain.usecase.database.datastore.language.GetLocaleLanguageAppUseCase
import com.example.domain.usecase.database.datastore.language.SetNewAppLanguageUseCase
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

    // authentication
    factory { ObserveIsAuthenticatedUseCase(repository = get()) }
    factory { ObserveOnboardingCompletedUseCase(repository = get()) }

    // onboarding
    factory { SetIsAuthenticatedUseCase(repository = get()) }
    factory { SetOnboardingCompletedUseCase(repository = get()) }

    // languages
    factory { GetAppLanguageUseCase(get()) }
    factory { SetNewAppLanguageUseCase(get()) }
    factory { ChangeAppLanguageUseCase(get()) }
    factory { GetLocaleLanguageAppUseCase(get()) }

}