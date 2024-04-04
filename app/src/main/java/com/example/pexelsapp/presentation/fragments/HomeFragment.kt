package com.example.pexelsapp.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.adapter.photo.PhotosAdapter
import com.example.pexelsapp.presentation.adapter.collection.CollectionsAdapter
import com.example.pexelsapp.presentation.viewModel.PaginationScrollListener
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: PhotoViewModel by activityViewModels()
    private val photosAdapter = PhotosAdapter()
    private val collectionsAdapter = CollectionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staggeredLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val linearLayoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        binding.photoRecyclerView.let {
            it.adapter = photosAdapter
            it.layoutManager = staggeredLayoutManager
            it.setHasFixedSize(true)
        }

        binding.collectionsRecyclerView.let {
            it.adapter = collectionsAdapter
            it.layoutManager = linearLayoutManager
        }

        setObservers(staggeredLayoutManager)
    }

    @SuppressLint("RestrictedApi")
    private fun setObservers(
        staggeredLayoutManager: StaggeredGridLayoutManager
    ) {

        viewModel.photoList.observe(viewLifecycleOwner) {
            if (it != null) {
                photosAdapter.list = it
                photosAdapter.notifyDataSetChanged()
            }
        }

        viewModel.collectionList.observe(viewLifecycleOwner) {
            try {
                if (it != null) {
                    collectionsAdapter.list = it
                    collectionsAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {
            }
        }

        collectionsAdapter.onClick = { it ->
            binding.photoRecyclerView.layoutManager!!.scrollToPosition(0)
            binding.searchView.setQuery(it.title.text.toString(), false)
        }
        photosAdapter.onClick = {
            viewModel.setDetailsState(it)
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment()
            binding.root.findNavController().navigate(action)
        }


        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            private var debounceJob: Job? = null
            private val DELAY: Long = 1000L
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getPhoto(query.toString())
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                debounceJob?.cancel()
                debounceJob =
                    this@HomeFragment.viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.Main) {
                        delay(DELAY)
                        viewModel.getPhoto(newText.toString())
                    }
                return true
            }
        })



        binding.tryAgainButton.setOnClickListener {
            viewModel.tryAgain()
        }
        binding.exploreButton.setOnClickListener {
            binding.searchView.setQuery("", false)
        }

        binding.photoRecyclerView.addOnScrollListener(object :
            PaginationScrollListener(staggeredLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMorePhoto()
            }

            override val isLoading: Boolean
                get() = viewModel.currentViewState().isLoading
        })
        binding.photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.photoRecyclerView.requestFocus()
                hideKeyboard(binding.root)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.isLoading) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }

        if (!viewState.noInternerConnection) {
            when (viewState.noResaultsFound) {
                true -> {
                    binding.photoRecyclerView.visibility = View.GONE
                    binding.stubNoQuery.visibility = View.VISIBLE
                    hideKeyboard(binding.root)
                }

                false -> {
                    binding.stubNoQuery.visibility = View.GONE
                    binding.photoRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        when (viewState.noInternerConnection) {
            true -> {
                binding.photoRecyclerView.visibility = View.GONE
                binding.stubNoQuery.visibility = View.GONE
                binding.stubNoInternet.visibility = View.VISIBLE
                hideKeyboard(binding.root)
            }

            false -> {
                if (!viewState.noResaultsFound) {
                    binding.stubNoInternet.visibility = View.GONE
                    binding.photoRecyclerView.visibility = View.VISIBLE
                }
            }
        }
        when (viewState.selectedCollection) {
            null -> {
                collectionsAdapter.selected = null
                collectionsAdapter.notifyDataSetChanged()
            }

            else -> {
                collectionsAdapter.selected = viewState.selectedCollection
                collectionsAdapter.notifyDataSetChanged()
            }
        }
        binding.searchView.setQuery(viewState.currentQuery, false)
        binding.progressBar.setProgressCompat(viewState.progress, true)
    }
}