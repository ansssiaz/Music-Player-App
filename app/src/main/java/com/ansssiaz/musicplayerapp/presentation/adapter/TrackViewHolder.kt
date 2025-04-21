package com.ansssiaz.musicplayerapp.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.ansssiaz.musicplayerapp.R
import com.ansssiaz.musicplayerapp.databinding.TracksItemBinding
import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(private val binding: TracksItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val radius =
        this.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)

    fun bind(track: TrackUiModel) {
        binding.trackTitle.text = track.title
        binding.artist.text = track.author
        if (track.imageUrl.isEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.no_image)
                .into(binding.trackImage)
        } else {
            Glide.with(binding.root)
                .load(track.imageUrl)
                .transform(RoundedCorners(radius))
                .error(R.drawable.no_image)
                .into(binding.trackImage)
        }
    }
}