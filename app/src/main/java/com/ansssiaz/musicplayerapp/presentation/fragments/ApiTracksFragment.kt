package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ansssiaz.musicplayerapp.databinding.FragmentApiTracksBinding

class ApiTracksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentApiTracksBinding.inflate(inflater, container, false)
        return binding.root
    }
}