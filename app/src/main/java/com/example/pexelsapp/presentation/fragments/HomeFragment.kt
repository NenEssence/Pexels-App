package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.data.PhotoRepositoryImpl
import com.example.pexelsapp.data.remote.RetrofitInstance
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.MainActivity
import com.example.pexelsapp.presentation.adapter.PhotoAdapter
import com.example.pexelsapp.presentation.viewModel.PaginationScrollListener
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import com.example.pexelsapp.presentation.viewModel.PhotoViewModelFactory


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

        val adapter = PhotoAdapter()


        binding.homeRecyclerView.adapter = adapter
        binding.homeRecyclerView.layoutManager = staggeredLayoutManager
        binding.homeRecyclerView.setHasFixedSize(true)

        binding.homeRecyclerView.addOnScrollListener(object : PaginationScrollListener(staggeredLayoutManager){
            override fun loadMoreItems() {
                Log.d("SCROLL","Triggered")
            }
            override val isLastPage: Boolean
                get() = false
            override val isLoading: Boolean
                get() = false
        })

        binding.editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.getPhoto(binding.editText.text.toString())
                return@OnKeyListener true
            }
            false
        })

        viewModel.photolist.observe(this, Observer {
            try {
                if (it != null) {
                    adapter.list = it.body()?.photos as ArrayList<Photo>
                    Log.d("ELEMENT","${adapter.itemCount}")
                    adapter.notifyDataSetChanged()
                }
//                adapter.differ.submitList(it?.body()?.photos?.toMutableList())
            } catch (_: Exception) {

            }
        })
    }
}