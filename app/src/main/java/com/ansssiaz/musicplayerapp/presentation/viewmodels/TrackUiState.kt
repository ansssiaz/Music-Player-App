package com.ansssiaz.musicplayerapp.presentation.viewmodels

import com.ansssiaz.musicplayerapp.utils.Status

data class TrackUiState (
    val tracks: List<TrackUiModel> = emptyList(),
    val track: TrackUiModel? = null,
    val status: Status = Status.Idle,
){
    val isEmptyLoading: Boolean = status == Status.Loading && tracks.isEmpty()
    val isError: Boolean
        get() = status is Status.Error && tracks.isEmpty()
}