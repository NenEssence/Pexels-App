package com.example.pexelsapp.domain

import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.domain.model.CollectionsResponse
import com.example.pexelsapp.domain.model.PexelsApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface PhotoRepository {
    suspend fun insertPhoto(photo: PhotoDbEntity)
    suspend fun deletePhoto(id: Int)
    suspend fun findPhotoById(id:Int): PhotoDbEntity?
    fun getAllPhotos(): Flow<List<PhotoDbEntity>>

    suspend fun loadPhoto(page: Int,query: String): Response<PexelsApiResponse>
    suspend fun loadFeaturedCollections(): Response<CollectionsResponse>
    suspend fun loadCuratedPhoto(page:Int): Response<PexelsApiResponse>
}