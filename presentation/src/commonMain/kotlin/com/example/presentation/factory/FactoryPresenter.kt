package com.example.presentation.factory

import com.example.domain.usecase.datastore.LanguageUseCase
import com.example.domain.usecase.authentication.LoginUseCase
import com.example.domain.usecase.authentication.RegisterUseCase
import com.example.presentation.presenter.authentication.LoginPresenter
import com.example.presentation.presenter.authentication.RegisterPresenter

fun createLoginPresenter(
    loginUseCase: LoginUseCase,
    languageUseCase: LanguageUseCase
): LoginPresenter = LoginPresenter(loginUseCase, languageUseCase)


fun createRegisterPresenter(
    registerUseCase: RegisterUseCase
) : RegisterPresenter = RegisterPresenter(registerUseCase)