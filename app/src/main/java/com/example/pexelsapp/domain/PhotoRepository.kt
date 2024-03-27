package com.example.pexelsapp.domain

import androidx.lifecycle.LiveData
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.data.remote.model.PexelsApiEntity
import com.example.pexelsapp.data.remote.model.Photo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface PhotoRepository {
//    suspend fun insertPhoto(photo: PhotoDbEntity)
//    suspend fun deletePhoto(photo: PhotoDbEntity)
//    fun getAllPhotos() : LiveData<List<PhotoDbEntity>>

    suspend fun loadPhoto(query: String): Response<PexelsApiEntity>
}