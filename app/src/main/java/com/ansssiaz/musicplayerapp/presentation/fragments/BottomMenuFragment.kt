package com.ansssiaz.musicplayerapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ansssiaz.musicplayerapp.R
import com.ansssiaz.musicplayerapp.databinding.FragmentBottomMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentBottomMenuBinding.inflate(inflater, container, false)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomMenu.setupWithNavController(navController)
        return binding.root
    }
}