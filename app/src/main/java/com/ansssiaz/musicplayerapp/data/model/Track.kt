package com.ansssiaz.musicplayerapp.data.model

import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksResponse(
    val tracks: Tracks
)

@Serializable
data class Tracks(
    val data: List<Track>
)

@Serializable
data class Track(
    val id: Long,
    val title: String,
    @SerialName("title_short")
    val titleShort: String,
    @SerialName("title_version")
    val titleVersion: String? = null,
    val link: String,
    val duration: Int,
    val rank: Int,
    @SerialName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover")
    val explicitContentCover: Int,
    val preview: String,
    @SerialName("md5_image")
    val md5Image: String,
    val position: Int,
    val artist: Artist,
    val album: Album,
    val type: String
)

fun Track.toUiModel(): TrackUiModel {
    val imageUrl = "https://cdn-images.dzcdn.net/images/cover/$md5Image/250x250-000000-80-0-0.jpg"
    return TrackUiModel(
        id = id,
        link = link,
        imageUrl = imageUrl,
        title = title,
        author = artist.name,
        isDownloaded = false
    )
}