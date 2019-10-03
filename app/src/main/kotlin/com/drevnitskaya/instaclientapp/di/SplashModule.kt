package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.domain.auth.CheckAuthStateUseCase
import com.drevnitskaya.instaclientapp.domain.auth.CheckAuthStateUseCaseImpl
import com.drevnitskaya.instaclientapp.presentation.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    factory<CheckAuthStateUseCase> {
        CheckAuthStateUseCaseImpl(authLocalRepository = get())
    }
    viewModel { SplashViewModel(checkAuthStateUseCase = get()) }
}