package com.ansssiaz.musicplayerapp.data.api

import com.ansssiaz.musicplayerapp.data.model.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {
    @GET("chart")
    suspend fun getTracks(): TracksResponse

    @GET("search")
    suspend fun search(@Query("q") query: String): TracksResponse
}