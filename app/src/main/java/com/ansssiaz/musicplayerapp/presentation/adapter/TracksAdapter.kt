package com.ansssiaz.musicplayerapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ansssiaz.musicplayerapp.databinding.TracksItemBinding
import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel

class TracksAdapter(
    private val listener: TracksListener,
) : ListAdapter<TrackUiModel, TrackViewHolder>(TrackDiffCallback()) {

    interface TracksListener {
        fun onTrackClicked(track: TrackUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TracksItemBinding.inflate(layoutInflater, parent, false)
        val viewHolder = TrackViewHolder(binding)
        binding.root.setOnClickListener {
            listener.onTrackClicked(getItem(viewHolder.getAbsoluteAdapterPosition()))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}