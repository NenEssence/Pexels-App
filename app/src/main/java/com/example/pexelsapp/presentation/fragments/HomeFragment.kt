package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.MainActivity
import com.example.pexelsapp.presentation.adapter.photo.PhotosAdapter
import com.example.pexelsapp.presentation.adapter.tag.CollectionsAdapter
import com.example.pexelsapp.presentation.viewModel.PaginationScrollListener
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: PhotoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

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

        viewModel.photoList.observe(this, Observer {
            try {
                if (it != null) {
                    photosAdapter.list = it
                    Log.d("ELEMENT", "${photosAdapter.itemCount}")
                    photosAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {
            }
        })

        viewModel.collectionList.observe(this, Observer {
            try {
                if (it != null) {
                    collectionsAdapter.list = it
                    collectionsAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {
            }
        })

        collectionsAdapter.onClick = {
            viewModel.getPhoto(it.title.text.toString())
            binding.editText.setText(it.title.text.toString())
        }
        photosAdapter.onClick = {
            viewModel.setDetailsState(it)
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment()
            binding.root.findNavController().navigate(action)
        }

        viewModel.viewState.observe(this, Observer<PhotoViewModel.ViewState> {
            render(it)
        })

        binding.editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.getPhoto(binding.editText.text.toString())
                return@OnKeyListener true
            }
            false
        })

        binding.photoRecyclerView.addOnScrollListener(object :
            PaginationScrollListener(staggeredLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadMorePhoto()
                Log.d("listener", "OnScroll")
            }

            override val isLastPage: Boolean
                get() = viewModel.currentViewState().isLastPage
            override val isLoading: Boolean
                get() = viewModel.currentViewState().isLoading
        })
    }

    private fun render(viewState: PhotoViewModel.ViewState) {
        when (viewState.isLoading) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
        if (viewState.selectedCollection != null) {
//            val vm = binding.collectionsRecyclerView.layoutManager?.findViewByPosition(viewState.selectedCollection)
//            vm?.findViewById<CardView>(R.id.card)?.setCardBackgroundColor(R.color.red)
//            binding.collectionsRecyclerView.layoutManager?.findViewByPosition(viewState.selectedCollection)
//                ?.setBackgroundColor(R.color.red)
        }
        binding.progressBar.setProgressCompat(viewState.progress, true)
    }
}