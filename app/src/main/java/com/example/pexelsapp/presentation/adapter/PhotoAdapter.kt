package com.example.pexelsapp.presentation.adapter


import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.databinding.PhotoLayoutBinding


class PhotoAdapter : RecyclerView.Adapter<PhotoViewHolder>() {
    var list = ArrayList<Photo>()

    private val differCallback = object : DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return  oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.itemView.layoutParams.height = differ.currentList[position].height - differ.currentList[position].width - Resources.getSystem().displayMetrics.widthPixels / 2
        Glide.with(holder.itemView.context).load(differ.currentList[position].src.portrait)
            .centerCrop()
            .into(holder.image)
        Log.d("SUP", "${differ.currentList.size}")
        holder.setIsRecyclable(false)
        holder.itemView.setOnClickListener {
            Log.d("-->", "Click")
        }
    }

    override fun getItemCount() = differ.currentList.size
}