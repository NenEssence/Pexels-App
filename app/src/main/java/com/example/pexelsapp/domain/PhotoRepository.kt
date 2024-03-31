package com.example.pexelsapp.domain

import com.example.pexelsapp.data.remote.model.PexelsApiResponse
import retrofit2.Response

interface PhotoRepository {
//    suspend fun insertPhoto(photo: PhotoDbEntity)
//    suspend fun deletePhoto(photo: PhotoDbEntity)
//    fun getAllPhotos() : LiveData<List<PhotoDbEntity>>

    suspend fun loadPhoto(query: String): Response<PexelsApiResponse>
    suspend fun loadMorePhoto(page: Int, query: String): Response<PexelsApiResponse>
}