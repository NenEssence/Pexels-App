package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.FragmentDetailsBinding
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: PhotoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }

        viewModel.detailsPhoto.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.src.portrait).centerCrop().into(binding.detailsImage)
        }
        binding.author.text = viewModel.detailsPhoto.value?.photographer
        viewModel.checkBookmarked()
        binding.buttonDownload.setOnClickListener {
            viewModel.saveImage(binding.detailsImage.drawable)
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it.isToastDownload) {
                true -> Toast.makeText(this.context, "Picture saved", Toast.LENGTH_SHORT).show()
                false -> {}
            }
        }
        binding.buttonAddBookmark.setOnClickListener {
            viewModel.detailsPhoto.value?.let { it1 -> viewModel.bookmarkPhoto(it1) }
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.isBookmarked) {
            true -> binding.buttonAddBookmark.setImageResource(R.drawable.bookmark_button_active)
            false -> binding.buttonAddBookmark.setImageResource(R.drawable.bookmark_button_inactive)
        }
    }

}