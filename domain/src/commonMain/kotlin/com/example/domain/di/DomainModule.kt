package com.example.domain.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.login.LoginUseCase
import com.example.domain.usecase.register.RegisterUseCase
import com.example.domain.usecase.reset.ResetPasswordUseCase
// ── DataStore — Authentication ────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.db.datastore.authentication.SetIsAuthenticatedUseCase
// ── DataStore — Onboarding ────────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.onboarding.ObserveOnboardingCompletedUseCase
import com.example.domain.usecase.db.datastore.onboarding.SetOnboardingCompletedUseCase
// ── DataStore — Language ──────────────────────────────────────────────────────
// ── Progression ───────────────────────────────────────────────────────────────
import com.example.domain.usecase.progression.BuildProgressionInsightUseCase
import com.example.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import com.example.domain.usecase.progression.GetProgressionDataUseCase
import com.example.domain.usecase.progression.GetTrainingSplitsUseCase
// ── Wiki ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.wiki.GetWikiArticlesUseCase
import com.example.domain.usecase.wiki.SearchWikiArticlesUseCase
import com.example.domain.usecase.wiki.ToggleBookmarkUseCase
// ── Friends ───────────────────────────────────────────────────────────────────

// ── Plan ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.activatePlan.ActivatePlanUseCase
import com.example.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetCurrentLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetSystemLocaleUseCase
import com.example.domain.usecase.db.datastore.trial.ObserveIsTrialCompletedUseCase
import com.example.domain.usecase.db.datastore.trial.SetIsTrialCompletedUseCase
import org.koin.dsl.module



val AuthUseCase = module {
    factory {
        LoginUseCase(
            userRepository = get(),
            authRepository = get()
        )
    }
    factory {
        RegisterUseCase(
            userRepository = get(),
            authRepository = get()
        )
    }
    factory {
        ResetPasswordUseCase(authRepository = get())
    }
}
val datastoreUseCase = module {
    // ── DataStore — Authentication ────────────────────────────────────────────
    factory { ObserveIsAuthenticatedUseCase(repository = get()) }
    factory { SetIsAuthenticatedUseCase(repository = get()) }

    // ── DataStore — Onboarding ────────────────────────────────────────────────
    factory { ObserveOnboardingCompletedUseCase(repository = get()) }

    factory { SetOnboardingCompletedUseCase(repository = get()) }

    // ── DataStore — Trial ─────────────────────────────────────────────────────
    factory { ObserveIsTrialCompletedUseCase(repository = get()) }
    factory { SetIsTrialCompletedUseCase(repository = get()) }

    // ── DataStore — Language ──────────────────────────────────────────────────
    factory { GetAppLanguageUseCase(get()) }
    factory { GetSystemLocaleUseCase(get()) }
    factory { ChangeAppLanguageUseCase(get()) }
    factory { GetCurrentLanguageUseCase(get()) }
}
val progressionUseCase = module {
    factory { GetTrainingSplitsUseCase(exerciseRepository = get()) }
    factory { GetExercisesByTrainingSplitUseCase(exerciseRepository = get()) }
    factory { GetProgressionDataUseCase(progressionRepository = get()) }
    factory { BuildProgressionInsightUseCase() }
}
val wikiUseCase = module {
    factory { GetWikiArticlesUseCase(get()) }
    factory { SearchWikiArticlesUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
}
val planUseCase = module {
    factory { ActivatePlanUseCase(get()) }
}

val domainModule = listOf(
    AuthUseCase,
    datastoreUseCase,
    progressionUseCase,
    wikiUseCase,
    planUseCase
)