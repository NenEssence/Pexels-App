package com.example.pexelsapp.presentation.adapter.collection

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.FeaturedCollectionLayoutBinding

class CollectionsViewHolder(itemBinding: FeaturedCollectionLayoutBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    var title: TextView = itemBinding.title
    var card: CardView = itemBinding.card
}

