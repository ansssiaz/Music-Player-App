package com.ansssiaz.musicplayerapp.domain.repository

import com.ansssiaz.musicplayerapp.data.model.CurrentTrack
import com.ansssiaz.musicplayerapp.data.model.Track

interface TracksRepository {
    suspend fun getTracks(): List<Track>
    suspend fun searchTrack(query: String): List<Track>
    suspend fun getTrack(id: Long): CurrentTrack
}