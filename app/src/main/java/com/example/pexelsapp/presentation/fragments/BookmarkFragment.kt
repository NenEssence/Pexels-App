package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentBookmarkBinding
import com.example.pexelsapp.presentation.MainActivity
import com.example.pexelsapp.presentation.adapter.bookmark.BookmarksAdapter
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: PhotoViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        viewModel.getBookmarks().asLiveData().observe(viewLifecycleOwner, Observer {
            bookmarksAdapter.list = it
            viewModel.bookmarksCheck(it)
            bookmarksAdapter.notifyDataSetChanged()
        })

        bookmarksAdapter.onClick = {
            viewModel.setDetailsState(it)
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToDetailsFragment()
            binding.root.findNavController().navigate(action)
        }

        binding.exploreButton.setOnClickListener{
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToHomeFragment()
            binding.root.findNavController().navigate(action)
        }
        viewModel.viewState.observe(viewLifecycleOwner, Observer<PhotoViewModel.ViewState> {
            render(it)
        })
    }

    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.noBookmarksFound) {
            true -> binding.bookmarkRecyclerView.visibility = View.GONE
            false -> binding.bookmarkRecyclerView.visibility = View.VISIBLE
        }
    }
}