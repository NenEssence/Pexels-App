package com.example.pexelsapp.presentation.adapter.photo

import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.PhotoLayoutBinding

class PhotosViewHolder(private var itemBinding: PhotoLayoutBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    var image: ImageView = itemBinding.photoImage
    init{
        itemView.setOnClickListener{
            Log.d("Click-->", itemBinding.toString())
        }
    }
}