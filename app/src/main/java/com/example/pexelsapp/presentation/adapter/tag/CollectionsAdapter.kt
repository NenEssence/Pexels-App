package com.example.pexelsapp.presentation.adapter.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.data.remote.model.FeaturedCollection
import com.example.pexelsapp.databinding.PhotoLayoutBinding

class CollectionsAdapter: RecyclerView.Adapter<CollectionsViewHolder>() {
    var list = emptyList<FeaturedCollection>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}