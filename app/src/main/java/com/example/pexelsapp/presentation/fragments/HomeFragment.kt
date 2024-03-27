package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.data.PhotoRepositoryImpl
import com.example.pexelsapp.data.remote.RetrofitInstance
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.adapter.PhotoAdapter
import com.example.pexelsapp.presentation.viewModel.PhotoViewModel
import com.example.pexelsapp.presentation.viewModel.PhotoViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.homeRecyclerView.layoutManager = GridLayoutManager(this.context,2)
        val adapter = PhotoAdapter()
        binding.homeRecyclerView.adapter = adapter

        val staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.homeRecyclerView.layoutManager = staggeredLayoutManager

        val repository = PhotoRepositoryImpl(RetrofitInstance.api)
        val viewModelFactory = PhotoViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]

        binding.homeRecyclerView.setHasFixedSize(true)



        viewModel.photolist.observe(this, Observer {
            try {
                adapter.list = it.body()?.photos as ArrayList<Photo>
                adapter.notifyDataSetChanged()
            } catch (_: Exception) {

            }
        })
    }
}