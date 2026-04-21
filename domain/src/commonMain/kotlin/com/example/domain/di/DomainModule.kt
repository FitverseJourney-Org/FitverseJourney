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
import com.example.domain.usecase.progression.BuildProgressionInsightUseCase
import com.example.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import com.example.domain.usecase.progression.GetProgressionDataUseCase
import com.example.domain.usecase.progression.GetTrainingSplitsUseCase
import com.example.domain.usecase.register.RegisterUseCase
import com.example.domain.usecase.register.ValidateRegisterPageAvatarUseCase
import com.example.domain.usecase.register.ValidateRegisterPageCredentialsUseCase
import com.example.domain.usecase.register.ValidateRegisterPageExperienceLevelUseCase
import com.example.domain.usecase.register.ValidateRegisterPageGenderUseCase
import com.example.domain.usecase.register.ValidateRegisterPageGoalsUseCase
import com.example.domain.usecase.register.ValidateRegisterPageIntroductionUseCase
import com.example.domain.usecase.register.ValidateRegisterPageMacrosUseCase
import com.example.domain.usecase.reset.ResetPasswordUseCase
import com.example.domain.usecase.wiki.GetWikiArticlesUseCase
import com.example.domain.usecase.wiki.SearchWikiArticlesUseCase
import com.example.domain.usecase.wiki.ToggleBookmarkUseCase
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

    // register
    factory { ValidateRegisterPageIntroductionUseCase() }
    factory { ValidateRegisterPageExperienceLevelUseCase() }
    factory { ValidateRegisterPageAvatarUseCase() }
    factory { ValidateRegisterPageMacrosUseCase() }
    factory { ValidateRegisterPageGenderUseCase() }
    factory { ValidateRegisterPageGoalsUseCase() }
    factory { ValidateRegisterPageCredentialsUseCase() }

    // progress
    factory { BuildProgressionInsightUseCase() }
    factory { GetExercisesByTrainingSplitUseCase(exerciseRepository = get()) }
    factory { GetProgressionDataUseCase(progressionRepository = get()) }
    factory { GetTrainingSplitsUseCase(exerciseRepository = get()) }

    // wiki
    factory { ToggleBookmarkUseCase(get()) }
    factory { SearchWikiArticlesUseCase(get()) }
    factory { GetWikiArticlesUseCase(get()) }

}