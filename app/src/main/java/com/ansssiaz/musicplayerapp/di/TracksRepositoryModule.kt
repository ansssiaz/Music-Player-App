package com.ansssiaz.musicplayerapp.di

import com.ansssiaz.musicplayerapp.data.repository.TracksRepositoryImpl
import com.ansssiaz.musicplayerapp.domain.repository.TracksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface TracksRepositoryModule {
    @Binds
    fun bindTracksRepositoryImpl(impl: TracksRepositoryImpl): TracksRepository
}