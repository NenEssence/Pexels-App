package com.example.pexelsapp.presentation.adapter.photo


import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.PhotoLayoutBinding
import com.example.pexelsapp.domain.model.Photo


class PhotosAdapter : RecyclerView.Adapter<PhotosViewHolder>() {
    var list = emptyList<Photo>()
    lateinit var onClick: (Photo) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val req = RequestOptions().placeholder(R.drawable.holder)

        //calculate new size
        holder.itemView.layoutParams.height =
            list[position].height - list[position].width - Resources.getSystem().displayMetrics.widthPixels / 2

        Glide.with(holder.itemView.context).load(list[position].src.portrait).apply(req)
            .centerCrop().into(holder.image)
        holder.setIsRecyclable(false)
        holder.itemView.setOnClickListener { onClick(list[position]) }
    }

    override fun getItemCount() = list.size
}