package com.example.presentation

import com.example.domain.usecase.datastore.LanguageUseCase
import com.example.domain.usecase.authentication.LoginUseCase
import com.example.domain.usecase.authentication.RegisterUseCase
import com.example.domain.usecase.authentication.ResetPasswordUseCase
import com.example.presentation.presenter.authentication.LoginPresenter
import com.example.presentation.presenter.authentication.RegisterPresenter
import com.example.presentation.presenter.authentication.ResetPasswordPresenter
import com.example.presentation.presenter.onboarding.OnboardingPresenter
import org.koin.java.KoinJavaComponent.get

// ---------- AUTH ----------

fun createLoginPresenterAndroid(): LoginPresenter {
    val useCaseLogin: LoginUseCase = get(LoginUseCase::class.java)
    val useCaseLanguage: LanguageUseCase = get(LanguageUseCase::class.java)
    return LoginPresenter(
        loginUseCase = useCaseLogin,
        languageUseCase = useCaseLanguage
    )
}

fun createRegisterPresenterAndroid(): RegisterPresenter {
    val useCase: RegisterUseCase = get(RegisterUseCase::class.java)
    return RegisterPresenter(useCase)
}

fun createResetPasswordPresenterAndroid(): ResetPasswordPresenter {
    val useCase: ResetPasswordUseCase = get(ResetPasswordUseCase::class.java)
    return ResetPasswordPresenter(useCase)
}

// ---------- ONBOARDING ----------

fun createOnboardingPresenterAndroid(): OnboardingPresenter {
    return OnboardingPresenter()
}

// ---------- SETUP LANGUAGE ----------
