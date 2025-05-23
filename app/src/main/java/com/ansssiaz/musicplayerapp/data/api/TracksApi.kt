package com.ansssiaz.musicplayerapp.data.api

import com.ansssiaz.musicplayerapp.data.model.CurrentTrack
import com.ansssiaz.musicplayerapp.data.model.Tracks
import com.ansssiaz.musicplayerapp.data.model.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TracksApi {
    @GET("chart")
    suspend fun getTracks(): TracksResponse

    @GET("search")
    suspend fun search(@Query("q") query: String): Tracks

    @GET("track/{id}")
    suspend fun getTrack(@Path("id") id: Long, ): CurrentTrack
}