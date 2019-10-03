package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.data.repository.InstaMediaRepository
import com.drevnitskaya.instaclientapp.data.repository.InstaMediaRepositoryImpl
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepositoryImpl
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCaseImpl
import com.drevnitskaya.instaclientapp.domain.GetRemoteMediaUseCase
import com.drevnitskaya.instaclientapp.domain.GetRemoteMediaUseCaseImpl
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

    factory<InstaMediaRepository> {
        InstaMediaRepositoryImpl(
            authLocalRepository = get(),
            remoteDataSource = get()
        )
    }

    factory<GetRemoteMediaUseCase> { GetRemoteMediaUseCaseImpl(mediaRepository = get()) }

    viewModel {
        ProfileViewModel(
            getProfileUseCase = get(),
            getMediaUseCase = get()
        )
    }
}