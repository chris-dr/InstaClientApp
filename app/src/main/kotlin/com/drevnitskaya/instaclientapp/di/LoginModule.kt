package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.domain.auth.*
import com.drevnitskaya.instaclientapp.presentation.login.LoginWebViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    factory<ComposeAuthUrlUseCase> { ComposeAuthUrlUseCaseImpl() }

    factory<ParseAuthCodeUseCase> { ParseAuthCodeUseCaseImpl() }

    factory<GetAccessTokenUseCase> {
        GetAccessTokenUseCaseImpl(
            authRepository = get()
        )
    }

    viewModel {
        LoginWebViewModel(
            networkStateProvider = get(),
            composeAuthUrlUseCase = get(),
            parseAuthCodeUseCase = get(),
            getAccessTokenUseCase = get()
        )
    }
}