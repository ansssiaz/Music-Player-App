package com.ansssiaz.musicplayerapp.di

import com.ansssiaz.musicplayerapp.data.api.TracksApi
import com.google.android.exoplayer2.BuildConfig
import com.ansssiaz.musicplayerapp.data.api.RetrofitFactory
import okhttp3.logging.HttpLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideOkHttp() = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .build()
            )
        }
        .let {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            } else {
                it
            }
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = RetrofitFactory.createRetrofit(okHttpClient)

    @Provides
    fun provideTracksApi(retrofit: Retrofit): TracksApi = retrofit.create()
}