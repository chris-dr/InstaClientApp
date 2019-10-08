package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import com.drevnitskaya.instaclientapp.data.repository.FeedRepositoryImpl
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepositoryImpl
import com.drevnitskaya.instaclientapp.domain.*
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCaseImpl
import com.drevnitskaya.instaclientapp.presentation.profile.ProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            tokenLocalDataSource = get(),
            profileRemoteDataSource = get()
        )
    }

    factory<GetProfileUseCase> { GetProfileUseCaseImpl(profileRepository = get()) }

    factory<FeedRepository> {
        FeedRepositoryImpl(
            tokenLocalDataSource = get(),
            feedRemoteDataSource = get()
        )
    }

    factory<LoadInitialFeedUseCase> { LoadInitialFeedUseCaseImpl(feedRepository = get()) }

    factory<GetMoreFeedUseCase> { GetMoreFeedUseCaseImpl(feedRepository = get()) }

    factory<LogoutUseCase> { LogoutUseCaseImpl(authRepository = get()) }

    viewModel {
        ProfileViewModel(
            networkStateProvider = get(),
            getProfileUseCase = get(),
            getFeedUseCase = get(),
            getMoreFeedUseCase = get(),
            logoutUseCase = get()
        )
    }
}