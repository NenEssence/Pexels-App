package com.example.pexelsapp.presentation.adapter.collection

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.FeaturedCollectionLayoutBinding
import com.example.pexelsapp.domain.model.FeaturedCollection

class CollectionsAdapter : RecyclerView.Adapter<CollectionsViewHolder>() {
    var list = emptyList<FeaturedCollection>()
    lateinit var onClick: (CollectionsViewHolder) -> Unit
    var selected: Int? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            FeaturedCollectionLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        val current = list[position]
        if (position == selected) {
            holder.card.setCardBackgroundColor(Color.parseColor("#BB1020"))
            holder.title.setTextColor(Color.parseColor("#F3F5F9"))
        } else {
            holder.card.setCardBackgroundColor(Color.parseColor("#F3F5F9"))
            holder.title.setTextColor(Color.parseColor("#1E1E1E"))
        }
        holder.title.text = current.title
        holder.itemView.setOnClickListener { onClick(holder) }
    }
}