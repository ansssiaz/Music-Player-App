package com.ansssiaz.musicplayerapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel

class TrackDiffCallback: DiffUtil.ItemCallback<TrackUiModel>() {
    override fun areItemsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean = oldItem == newItem
}