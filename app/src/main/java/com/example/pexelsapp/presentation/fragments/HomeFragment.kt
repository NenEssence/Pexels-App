package com.example.pexelsapp.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.adapter.photo.PhotosAdapter
import com.example.pexelsapp.presentation.adapter.tag.CollectionsAdapter
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val staggeredLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val linearLayoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        val photosAdapter = PhotosAdapter()
        val collectionsAdapter = CollectionsAdapter()


        binding.photoRecyclerView.adapter = photosAdapter
        binding.photoRecyclerView.layoutManager = staggeredLayoutManager
        binding.photoRecyclerView.setHasFixedSize(true)

        binding.collectionsRecyclerView.adapter = collectionsAdapter
        binding.collectionsRecyclerView.layoutManager = linearLayoutManager
        setObservers(photosAdapter, collectionsAdapter, staggeredLayoutManager)
    }

    @SuppressLint("RestrictedApi")
    private fun setObservers(
        photosAdapter: PhotosAdapter,
        collectionsAdapter: CollectionsAdapter,
        staggeredLayoutManager: StaggeredGridLayoutManager
    ) {

        viewModel.photoList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                photosAdapter.list = it
                photosAdapter.notifyDataSetChanged()
            }
        })

        viewModel.collectionkList.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    collectionsAdapter.list = it
                    collectionsAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {
            }
        })

        collectionsAdapter.onClick = {
            binding.photoRecyclerView.layoutManager!!.scrollToPosition(0)
            binding.searchView.requestFocus()
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
            private val DELAY: Long = 2000L
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getPhoto(query.toString())
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                debounceJob?.cancel()
                debounceJob = this@HomeFragment.viewLifecycleOwner.lifecycle.coroutineScope
                    .launch(Dispatchers.Main) {
                        delay(DELAY)
                        viewModel.getPhoto(newText.toString())
                    }
                return true
            }
        })



        binding.tryAgainButton.setOnClickListener {
            viewModel.tryAgain()
        }
        binding.exploreButton.setOnClickListener{
            binding.searchView.setQuery("",false)
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

        viewModel.viewState.observe(viewLifecycleOwner, Observer<PhotoViewModel.ViewState> {
            render(it)
        })
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
//        binding.searchView.setQuery(viewState.currentQuery, false)

        binding.progressBar.setProgressCompat(viewState.progress, true)
    }
}