package com.example.pexelsapp.data

import com.example.pexelsapp.data.local.PhotoDatabase
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.domain.PhotoRepository

class PhotoRepositoryImpl(private val db: PhotoDatabase): PhotoRepository {
    suspend override fun insertPhoto(photo: PhotoDbEntity) = db.getPhotoDao().insertPhoto(photo)
    suspend override fun deletePhoto(photo: PhotoDbEntity) = db.getPhotoDao().deletePhoto(photo)

    override fun getAllPhotos() = db.getPhotoDao().getAllPhotos()
}