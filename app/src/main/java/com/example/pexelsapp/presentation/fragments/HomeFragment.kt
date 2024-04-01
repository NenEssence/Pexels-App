package com.example.pexelsapp.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.R
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

    private fun setObservers(
        photosAdapter: PhotosAdapter,
        collectionsAdapter: CollectionsAdapter,
        staggeredLayoutManager: StaggeredGridLayoutManager
    ) {

        viewModel.photoList.observe(viewLifecycleOwner, Observer {
            try {
                if (it != null) {
                    photosAdapter.list = it
                    photosAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {
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
            viewModel.getPhoto(it.title.text.toString())
            binding.editText.setText(it.title.text.toString())
        }
        photosAdapter.onClick = {
            viewModel.setDetailsState(it)
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment()
            binding.root.findNavController().navigate(action)
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer<PhotoViewModel.ViewState> {
            render(it)
        })

        binding.editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.photoRecyclerView.layoutManager!!.scrollToPosition(0)
                viewModel.getPhoto(binding.editText.text.toString())
                return@OnKeyListener true
            }
            false
        })
        binding.editText.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.editText.addTextChangedListener(onQueryChangeListener)
                binding.clearButton.visibility = View.VISIBLE
            } else {
                binding.editText.removeTextChangedListener(onQueryChangeListener)
                binding.clearButton.visibility = View.GONE
            }
        }

        binding.clearButton.setOnClickListener {
            binding.editText.text.clear()
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
    }
    private val onQueryChangeListener = object : TextWatcher {
        private var debounceJob: Job? = null
        private val DELAY: Long = 2000L

        override fun afterTextChanged(s: Editable?) {
            debounceJob?.cancel()
            debounceJob = this@HomeFragment.viewLifecycleOwner.lifecycle.coroutineScope
                .launch(Dispatchers.Main) {
                    delay(DELAY)
                    viewModel.getPhoto(s?.toString() ?: "")
                }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    @SuppressLint("ResourceAsColor")
    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.isLoading) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
        if (viewState.selectedCollection != null) {
        }
        when(viewState.noResaultsFound){
            true -> {
                binding.photoRecyclerView.visibility = View.GONE
                binding.collectionsRecyclerView.requestFocus()
            }
            false -> binding.photoRecyclerView.visibility = View.VISIBLE
        }

        binding.editText.setText(viewState.currentQuery)
        binding.progressBar.setProgressCompat(viewState.progress, true)
    }
}