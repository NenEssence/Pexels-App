package com.example.pexelsapp.presentation.adapter


import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.databinding.PhotoLayoutBinding


class PhotoAdapter : RecyclerView.Adapter<PhotoViewHolder>() {
    var list = ArrayList<Photo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.itemView.layoutParams.height = list[position].height - list[position].width - Resources.getSystem().displayMetrics.widthPixels / 2
        Glide.with(holder.itemView.context).load(list[position].src.portrait)
            .centerCrop()
            .into(holder.image)


        Log.d("PEXELS_RESPONSE", list[position].toString())
        holder.itemView.setOnClickListener {
            Log.d("-->", "Click")
        }
    }

    override fun getItemCount() = list.size
}