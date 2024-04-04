package com.example.pexelsapp.presentation.adapter.bookmark

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.BookmarkLayoutBinding

class BookmarksViewHolder(itemBinding: BookmarkLayoutBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    var image: ImageView = itemBinding.photoImage
    var author: TextView = itemBinding.author
}