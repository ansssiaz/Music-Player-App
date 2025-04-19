package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ansssiaz.musicplayerapp.databinding.FragmentLocalTracksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalTracksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLocalTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}