package com.example.pexelsapp.presentation.adapter.tag

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.FeaturedCollectionLayoutBinding

class CollectionsViewHolder(var itemBinding: FeaturedCollectionLayoutBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    var title: TextView = itemBinding.title
}
