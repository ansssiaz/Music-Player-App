package com.ansssiaz.musicplayerapp.data.repository

import com.ansssiaz.musicplayerapp.data.api.TracksApi
import com.ansssiaz.musicplayerapp.data.model.Track
import com.ansssiaz.musicplayerapp.domain.repository.TracksRepository
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(private val api: TracksApi) : TracksRepository {
    override suspend fun getTracks(): List<Track> = api.getTracks().tracks.data

    override suspend fun searchTrack(query: String): List<Track> {
        val formattedQuery = query.replace(" ", "+")
        return api.search(formattedQuery).data
    }
}