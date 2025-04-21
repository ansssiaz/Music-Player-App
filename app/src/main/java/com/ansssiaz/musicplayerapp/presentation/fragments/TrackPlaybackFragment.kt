@file:Suppress("DEPRECATION")

package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.ansssiaz.musicplayerapp.R
import com.ansssiaz.musicplayerapp.databinding.FragmentTrackPlaybackBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class TrackPlaybackFragment : Fragment() {
    companion object {
        const val ARG_TRACK_ID = "ARG_TRACK_ID"
        const val ARG_TRACK_TITLE = "ARG_TRACK_TITLE"
        const val ARG_ALBUM = "ARG_ALBUM"
        const val ARG_AUTHOR = "ARG_AUTHOR"
        const val ARG_TRACK_PREVIEW = "ARG_TRACK_PREVIEW"
        const val ARG_TRACK_IMAGE_URL = "ARG_TRACK_IMAGE"
        const val ARG_TRACKS_LIST = "ARG_TRACKS_LIST"
    }

    private lateinit var player: ExoPlayer
    private lateinit var binding: FragmentTrackPlaybackBinding
    private lateinit var handler: Handler
    private val updateProgress = object : Runnable {
        override fun run() {
            updateProgressBar()
            handler.postDelayed(this, 1000)
        }
    }

    private var currentTrackIndex = 0
    private lateinit var tracksList: List<Bundle>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackPlaybackBinding.inflate(inflater, container, false)
        initArguments()
        initPlayer()
        setupSeekBar()
        setupClickListeners()
        return binding.root
    }

    private fun initArguments() {
        val args = arguments ?: Bundle()
        tracksList = args.getParcelableArrayList(ARG_TRACKS_LIST) ?: emptyList()

        if (tracksList.isEmpty()) {
            binding.skip.isEnabled = false
            binding.skipPrevious.isEnabled = false
        }

        currentTrackIndex = tracksList.indexOfFirst {
            it.getLong("id") == args.getLong(ARG_TRACK_ID)
        }.coerceAtLeast(0)

        updateTrackInfo(
            title = args.getString(ARG_TRACK_TITLE),
            author = args.getString(ARG_AUTHOR),
            album = args.getString(ARG_ALBUM),
            imageUrl = args.getString(ARG_TRACK_IMAGE_URL)
        )
    }

    private fun updateTrackInfo(title: String?, author: String?, album: String?, imageUrl: String?) {
        binding.currentTrackTitle.text = title
        binding.authorName.text = author
        binding.albumTitle.text = album

        if (imageUrl.isNullOrEmpty()) {
            Glide.with(binding.root)
                .load(R.drawable.no_image)
                .into(binding.currentTrackImage)
        } else {
            Glide.with(binding.root)
                .load(imageUrl)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)))
                .error(R.drawable.no_image)
                .into(binding.currentTrackImage)
        }
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        player.addListener(playerListener)

        arguments?.getString(ARG_TRACK_PREVIEW)?.let { audioUri ->
            val media = MediaItem.fromUri(audioUri)
            player.setMediaItem(media)
            player.prepare()
            player.playWhenReady = true
        }

        handler = Handler(Looper.getMainLooper())
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_READY -> {
                    binding.seekbar.max = player.duration.toInt()
                    updateTimeDisplay()
                    handler.post(updateProgress)
                }
                Player.STATE_ENDED -> {
                    if (tracksList.isNotEmpty()) {
                        currentTrackIndex = (currentTrackIndex + 1) % tracksList.size
                        playTrackAtIndex(currentTrackIndex)
                    }
                }
                else -> {}
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                handler.post(updateProgress)
                binding.playPause.setImageResource(R.drawable.ic_pause)
            } else {
                handler.removeCallbacks(updateProgress)
            }
        }
    }

    private fun setupSeekBar() {
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var wasPlaying = false

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress.toLong())
                    updateTimeDisplay()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                wasPlaying = player.isPlaying
                if (wasPlaying) player.pause()
                handler.removeCallbacks(updateProgress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (wasPlaying) {
                    player.play()
                    handler.post(updateProgress)
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.playPause.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.playPause.setImageResource(R.drawable.ic_play)
            } else {
                if (player.playbackState == Player.STATE_ENDED) player.seekTo(0)
                player.play()
                binding.playPause.setImageResource(R.drawable.ic_pause)
                handler.post(updateProgress)
            }
        }

        binding.skip.setOnClickListener {
            if (tracksList.isNotEmpty()) {
                currentTrackIndex = (currentTrackIndex + 1) % tracksList.size
                playTrackAtIndex(currentTrackIndex)
            }
        }

        binding.skipPrevious.setOnClickListener {
            if (tracksList.isNotEmpty()) {
                currentTrackIndex = (currentTrackIndex - 1).mod(tracksList.size)
                playTrackAtIndex(currentTrackIndex)
            }
        }
    }

    private fun playTrackAtIndex(index: Int) {
        val track = tracksList[index]
        updateTrackInfo(
            title = track.getString("title"),
            author = track.getString("author"),
            album = track.getString("album"),
            imageUrl = track.getString("imageUrl")
        )

        player.stop()
        track.getString("preview")?.let { previewUrl ->
            player.setMediaItem(MediaItem.fromUri(previewUrl))
            player.prepare()
            player.play()
        }

        binding.seekbar.progress = 0
        binding.playPause.setImageResource(R.drawable.ic_pause)
    }

    private fun updateProgressBar() {
        if (::player.isInitialized && player.duration > 0) {
            binding.seekbar.progress = player.currentPosition.toInt()
            updateTimeDisplay()
        }
    }

    private fun updateTimeDisplay() {
        if (::player.isInitialized) {
            val currentPosition = player.currentPosition
            val duration = player.duration

            binding.playbackTime.text = formatTime(currentPosition)
            binding.trackDuration.text = formatTime(duration)
        }
    }

    private fun formatTime(timeMs: Long): String {
        val minutes = (timeMs / 1000) / 60
        val seconds = (timeMs / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        if (::player.isInitialized) {
            player.release()
        }
    }
}