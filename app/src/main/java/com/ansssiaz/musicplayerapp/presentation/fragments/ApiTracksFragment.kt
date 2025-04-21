package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ansssiaz.musicplayerapp.R
import com.ansssiaz.musicplayerapp.databinding.FragmentApiTracksBinding
import com.ansssiaz.musicplayerapp.presentation.adapter.TracksAdapter
import com.ansssiaz.musicplayerapp.presentation.viewmodels.TrackUiModel
import com.ansssiaz.musicplayerapp.presentation.viewmodels.TracksViewModel
import com.ansssiaz.musicplayerapp.utils.getErrorText
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ApiTracksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentApiTracksBinding.inflate(inflater, container, false)
        var snackbar: Snackbar? = null
        val viewModel by viewModels<TracksViewModel>()

        val tracksAdapter = TracksAdapter(
            object : TracksAdapter.TracksListener {
                override fun onTrackClicked(track: TrackUiModel) {
                    viewModel.getTrack(track.id)
                    val tracksList = ArrayList<Parcelable>().apply {
                        addAll(viewModel.state.value.tracks.map { currentTrack ->
                            bundleOf(
                                "id" to currentTrack.id,
                                "title" to currentTrack.title,
                                "preview" to currentTrack.preview,
                                "imageUrl" to currentTrack.imageUrl,
                                "album" to currentTrack.album,
                                "author" to currentTrack.author
                            )
                        }
                        )
                    }

                    requireParentFragment()
                        .requireParentFragment()
                        .findNavController()
                        .navigate(
                            R.id.action_bottomMenuFragment_to_trackPlaybackFragment,
                            bundleOf(
                                TrackPlaybackFragment.ARG_TRACK_ID to track.id,
                                TrackPlaybackFragment.ARG_TRACK_TITLE to track.title,
                                TrackPlaybackFragment.ARG_TRACK_PREVIEW to track.preview,
                                TrackPlaybackFragment.ARG_TRACK_IMAGE_URL to track.imageUrl,
                                TrackPlaybackFragment.ARG_ALBUM to track.album,
                                TrackPlaybackFragment.ARG_AUTHOR to track.author,
                                TrackPlaybackFragment.ARG_TRACKS_LIST to tracksList
                            )
                        )
                }
            }
        )

        binding.listOfTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfTracks.adapter = tracksAdapter

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchTracks(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.getTracks()
                } else {
                    viewModel.searchTracks(newText)
                }
                return true
            }
        })

        viewModel.getTracks()

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.isError) {
                    val errorText =
                        it.status.throwableOrNull?.getErrorText(requireContext()).toString()

                    if (snackbar == null) {
                        snackbar = Snackbar.make(
                            binding.root,
                            errorText,
                            Snackbar.LENGTH_INDEFINITE
                        ).apply {
                            setAction(getString(R.string.snackbar_action)) {
                                viewModel.getTracks()
                            }
                            setActionTextColor(getColor(requireContext(), R.color.violet))
                        }
                    } else {
                        snackbar?.setText(errorText)
                    }
                    snackbar?.show()
                } else {
                    snackbar?.dismiss()
                    snackbar = null
                }

                tracksAdapter.submitList(it.tracks)
                binding.progress.isVisible = it.isEmptyLoading
                binding.search.isVisible = !it.isError && !it.isEmptyLoading
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}