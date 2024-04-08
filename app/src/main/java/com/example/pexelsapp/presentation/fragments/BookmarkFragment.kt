package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentBookmarkBinding
import com.example.pexelsapp.presentation.adapter.bookmark.BookmarksAdapter
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: PhotoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { render(it) }

            }
        }
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val staggeredLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val bookmarksAdapter = BookmarksAdapter()

        binding.bookmarkRecyclerView.adapter = bookmarksAdapter
        binding.bookmarkRecyclerView.layoutManager = staggeredLayoutManager
        binding.bookmarkRecyclerView.setHasFixedSize(true)

        viewModel.getBookmarks().asLiveData().observe(viewLifecycleOwner) {
            bookmarksAdapter.list = it
            viewModel.bookmarksCheck(it)
            bookmarksAdapter.notifyDataSetChanged()
        }

        bookmarksAdapter.onClick = {
            viewModel.setDetailsState(it)
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToDetailsFragment()
            binding.root.findNavController().navigate(action)
        }

        binding.exploreButton.setOnClickListener {
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToHomeFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.noBookmarksFound) {
            true -> binding.bookmarkRecyclerView.visibility = View.GONE
            false -> binding.bookmarkRecyclerView.visibility = View.VISIBLE
        }
    }
}