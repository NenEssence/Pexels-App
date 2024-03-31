package com.example.pexelsapp.presentation.adapter.photo


import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.R
import com.example.pexelsapp.data.remote.model.Photo
import com.example.pexelsapp.databinding.PhotoLayoutBinding
import com.example.pexelsapp.presentation.adapter.tag.CollectionsViewHolder


class PhotosAdapter : RecyclerView.Adapter<PhotosViewHolder>() {
    var list = emptyList<Photo>()
    lateinit var onClick: (Photo) -> Unit

    private val differCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {

        val req = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        //calculate new size
        holder.itemView.layoutParams.height =
            list[position].height - list[position].width - Resources.getSystem().displayMetrics.widthPixels / 2

        Glide.with(holder.itemView.context).load(list[position].src.portrait)
            .apply(req)
            .centerCrop()
            .into(holder.image)
        holder.setIsRecyclable(false)
        holder.itemView.setOnClickListener{onClick(list[position])}
    }

//    override fun getItemCount() = differ.currentList.size
override fun getItemCount() = list.size
}