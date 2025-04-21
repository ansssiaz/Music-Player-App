package com.ansssiaz.musicplayerapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ansssiaz.musicplayerapp.data.model.toUiModel
import com.ansssiaz.musicplayerapp.domain.repository.TracksRepository
import com.ansssiaz.musicplayerapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(
    private val repository: TracksRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TrackUiState())
    val state = _state.asStateFlow()

    fun getTracks(){
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val tracks = repository.getTracks().map { it.toUiModel() }
                _state.update { it.copy(tracks = tracks, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun searchTracks(query: String) {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val tracks = repository.searchTrack(query).map { it.toUiModel() }
                _state.update { it.copy(tracks = tracks, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun getTrack(id: Long) {
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                val track = repository.getTrack(id).toUiModel()
                _state.update { it.copy(track = track, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }
}