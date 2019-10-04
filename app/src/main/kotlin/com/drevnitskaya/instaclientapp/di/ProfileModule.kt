package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import com.drevnitskaya.instaclientapp.data.repository.FeedRepositoryImpl
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepositoryImpl
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCaseImpl
import com.drevnitskaya.instaclientapp.domain.GetRemoteFeedUseCase
import com.drevnitskaya.instaclientapp.domain.GetRemoteFeedUseCaseImpl
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCaseImpl
import com.drevnitskaya.instaclientapp.presentation.profile.ProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            authLocalRepository = get(),
            remoteDataSource = get()
        )
    }

    factory<GetProfileUseCase> { GetProfileUseCaseImpl(profileRepository = get()) }

    factory<FeedRepository> {
        FeedRepositoryImpl(
            authLocalRepository = get(),
            remoteDataSource = get()
        )
    }

    factory<GetRemoteFeedUseCase> { GetRemoteFeedUseCaseImpl(feedRepository = get()) }

    factory<LogoutUseCase> { LogoutUseCaseImpl(profileRepository = get()) }

    viewModel {
        ProfileViewModel(
            getProfileUseCase = get(),
            getFeedUseCase = get(),
            logoutUseCase = get()
        )
    }
}