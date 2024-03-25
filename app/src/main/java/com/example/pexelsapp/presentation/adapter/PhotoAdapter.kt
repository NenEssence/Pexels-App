package com.example.pexelsapp.presentation.adapter

import android.provider.ContactsContract.Contacts.Photo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.PhotoLayoutBinding
import com.example.pexelsapp.presentation.fragments.HomeFragmentDirections

class PhotoAdapter : RecyclerView.Adapter<PhotoViewHolder>() {
    private val list = emptyList<Photo>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = list[position]
        holder.itemView.setOnClickListener{
            //TODO change direction
            val direction = HomeFragmentDirections.actionHomeFragmentToBookmarkFragment()
            it.findNavController().navigate(direction)
        }
    }

    override fun getItemCount() = list.size
}