package com.example.pexelsapp.presentation.adapter.bookmark


import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.R
import com.example.pexelsapp.domain.model.Photo
import com.example.pexelsapp.databinding.BookmarkLayoutBinding
import com.example.pexelsapp.presentation.adapter.tag.CollectionsViewHolder

class BookmarksAdapter : RecyclerView.Adapter<BookmarksViewHolder>() {
    var list = emptyList<Photo>()
    lateinit var onClick: (Photo) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        return BookmarksViewHolder(
                    BookmarkLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val req = RequestOptions().placeholder(R.drawable.holder)
        //calculate new size
        holder.itemView.layoutParams.height =
            list[position].height - list[position].width - Resources.getSystem().displayMetrics.widthPixels / 2

        Glide.with(holder.itemView.context).load(list[position].src.portrait)
            .apply(req)
            .centerCrop()
            .into(holder.image)
        holder.setIsRecyclable(false)
        holder.author.text = list[position].photographer
        holder.itemView.setOnClickListener{onClick(list[position])}
    }
}