package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
    @RequiresApi(Build.VERSION_CODES.P)
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
                    //playback track fragment
                }
            }
        )

        binding.listOfTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.listOfTracks.adapter = tracksAdapter

        binding.search.setQueryHint(getString(R.string.search_query_hint))

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