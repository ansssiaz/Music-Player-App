package com.ansssiaz.musicplayerapp.presentation.viewmodels

data class TrackUiModel(
    val id: Long,
    val link: String,
    val imageUrl: String,
    val title: String,
    val author: String,
    val isDownloaded: Boolean = false,
)