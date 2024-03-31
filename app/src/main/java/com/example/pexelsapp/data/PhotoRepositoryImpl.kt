package com.example.pexelsapp.data

import com.example.pexelsapp.data.local.PhotoDatabase
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.data.remote.PexelsApi
import com.example.pexelsapp.data.remote.model.PexelsApiResponse
import com.example.pexelsapp.domain.PhotoRepository
import retrofit2.Response

class PhotoRepositoryImpl( private val api: PexelsApi) :
    PhotoRepository {
//    override suspend fun insertPhoto(photo: PhotoDbEntity) = db.getPhotoDao().insertPhoto(photo)
//    override suspend fun deletePhoto(photo: PhotoDbEntity) = db.getPhotoDao().deletePhoto(photo)
//
//    override fun getAllPhotos() = db.getPhotoDao().getAllPhotos()

    override suspend fun loadPhoto(query: String) = api.getPhoto(query)
    override suspend fun loadMorePhoto(page: Int,query: String) = api.loadMorePhoto(page,query)
}