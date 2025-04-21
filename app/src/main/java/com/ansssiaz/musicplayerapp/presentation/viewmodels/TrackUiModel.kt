package com.ansssiaz.musicplayerapp.presentation.viewmodels

data class TrackUiModel(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val author: String,
    val album: String? = null,
    val albumImageUrl: String,
    val preview: String
)