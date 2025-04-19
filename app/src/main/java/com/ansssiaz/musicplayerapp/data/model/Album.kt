package com.ansssiaz.musicplayerapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Long,
    val title: String,
    val cover: String,
    @SerialName("cover_small")
    val coverSmall: String,
    @SerialName("cover_medium")
    val coverMedium: String,
    @SerialName("cover_big")
    val coverBig: String,
    @SerialName("cover_xl")
    val coverXl: String,
    @SerialName("md5_image")
    val md5Image: String,
    val tracklist: String,
    val type: String
)
