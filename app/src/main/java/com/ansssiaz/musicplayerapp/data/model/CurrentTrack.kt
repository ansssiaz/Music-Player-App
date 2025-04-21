package com.ansssiaz.musicplayerapp.data.model

import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentTrack(
    val id: Long,
    val readable: Boolean,
    val title: String,
    @SerialName("title_short")
    val titleShort: String,
    @SerialName("title_version")
    val titleVersion: String,
    val isrc: String,
    val link: String,
    val share: String,
    val duration: Int,
    @SerialName("track_position")
    val trackPosition: Int,
    @SerialName("disk_number")
    val diskNumber: Int,
    val rank: Int,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerialName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerialName("explicit_content_cover")
    val explicitContentCover: Int,
    val preview: String,
    val bpm: Int,
    val gain: Double,
    @SerialName("available_countries")
    val availableCountries: List<String>,
    val contributors: List<Contributor>,
    @SerialName("md5_image")
    val md5Image: String,
    @SerialName("track_token")
    val trackToken: String,
    val artist: Artist,
    val album: Album,
    val type: String
)

@Serializable
data class Contributor(
    val id: Long,
    val name: String,
    val link: String,
    val share: String,
    val picture: String,
    @SerialName("picture_small")
    val pictureSmall: String,
    @SerialName("picture_medium")
    val pictureMedium: String,
    @SerialName("picture_big")
    val pictureBig: String,
    @SerialName("picture_xl")
    val pictureXl: String,
    val radio: Boolean,
    val tracklist: String,
    val type: String,
    val role: String
)

fun CurrentTrack.toUiModel(): TrackUiModel {
    val imageUrl = "https://cdn-images.dzcdn.net/images/cover/$md5Image/250x250-000000-80-0-0.jpg"
    return TrackUiModel(
        id = id,
        title = title,
        imageUrl = imageUrl,
        author = artist.name,
        album = album.title,
        albumImageUrl = album.cover,
        preview = preview
    )
}