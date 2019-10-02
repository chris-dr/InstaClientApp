package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCase
import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCaseImpl
import com.drevnitskaya.instaclientapp.presentation.login.LoginWebViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    factory<ComposeAuthUrlUseCase> { ComposeAuthUrlUseCaseImpl() }

    viewModel { LoginWebViewModel(composeAuthUrlUseCase = get()) }
}