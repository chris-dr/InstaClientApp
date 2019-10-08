package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import com.drevnitskaya.instaclientapp.data.repository.FeedRepositoryImpl
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepositoryImpl
import com.drevnitskaya.instaclientapp.data.source.local.dao.FeedLocalDataSource
import com.drevnitskaya.instaclientapp.data.source.local.dao.ProfileLocalDataSource
import com.drevnitskaya.instaclientapp.domain.*
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCaseImpl
import com.drevnitskaya.instaclientapp.framework.db.InstaAppDataBase
import com.drevnitskaya.instaclientapp.presentation.profile.ProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single<ProfileLocalDataSource> { get<InstaAppDataBase>().profileDao() }

    single<FeedLocalDataSource> { get<InstaAppDataBase>().feedDao() }

    factory<ProfileRepository> {
        ProfileRepositoryImpl(
            tokenLocalDataSource = get(),
            profileRemoteDataSource = get(),
            profileLocalDataSource = get()
        )
    }

    factory<GetProfileUseCase> { GetProfileUseCaseImpl(profileRepository = get()) }

    factory<FeedRepository> {
        FeedRepositoryImpl(
            tokenLocalDataSource = get(),
            feedRemoteDataSource = get(),
            feedLocalDataSource = get()
        )
    }

    factory<LoadInitialFeedUseCase> { LoadInitialFeedUseCaseImpl(feedRepository = get()) }

    factory<LoadMoreFeedUseCase> { LoadMoreFeedUseCaseImpl(feedRepository = get()) }

    factory<LogoutUseCase> {
        LogoutUseCaseImpl(
            authRepository = get(),
            profileRepository = get(),
            feedRepository = get()
        )
    }

    viewModel {
        ProfileViewModel(
            networkStateProvider = get(),
            getProfileUseCase = get(),
            loadInitialFeedUseCase = get(),
            loadMoreFeedUseCase = get(),
            logoutUseCase = get()
        )
    }
}