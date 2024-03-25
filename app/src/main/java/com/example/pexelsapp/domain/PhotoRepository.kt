package com.example.pexelsapp.domain

import androidx.lifecycle.LiveData
import com.example.pexelsapp.data.local.PhotoDbEntity

interface PhotoRepository {
    suspend fun insertPhoto(photo: PhotoDbEntity)
    suspend fun deletePhoto(photo: PhotoDbEntity)
    fun getAllPhotos() : LiveData<List<PhotoDbEntity>>
}