package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pexelsapp.databinding.FragmentBookmarkBinding


class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container,false)
        return binding.root
    }
}