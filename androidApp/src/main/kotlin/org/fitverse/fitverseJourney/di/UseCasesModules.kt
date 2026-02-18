package org.fitverse.fitverseJourney.di

import com.example.domain.usecase.datastore.LanguageUseCase
import com.example.domain.usecase.authentication.LoginUseCase
import com.example.domain.usecase.authentication.RegisterUseCase
import com.example.domain.usecase.authentication.ResetPasswordUseCase
import org.koin.dsl.module


val useCasesModule = module {
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
    factory {
        LanguageUseCase(
            languageRepository = get()
        )
    }
}
