package com.example.pexelsapp.presentation.adapter.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.domain.model.FeaturedCollection
import com.example.pexelsapp.databinding.FeaturedCollectionLayoutBinding

class CollectionsAdapter : RecyclerView.Adapter<CollectionsViewHolder>() {
    var list = emptyList<FeaturedCollection>()
    lateinit var onClick: (CollectionsViewHolder) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            FeaturedCollectionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        val current = list[position]
        holder.title.text = current.title
        holder.itemView.setOnClickListener{onClick(holder)}
    }
}