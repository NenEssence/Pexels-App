package com.example.pexelsapp.data

import com.example.pexelsapp.data.local.PhotoDao
import com.example.pexelsapp.data.local.PhotoDbEntity
import com.example.pexelsapp.data.remote.PexelsApi
import com.example.pexelsapp.domain.PhotoRepository
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl(private val photoDao: PhotoDao, private val api: PexelsApi) :
    PhotoRepository {
    override suspend fun insertPhoto(photo: PhotoDbEntity) = photoDao.insertPhoto(photo)
    override suspend fun deletePhoto(id: Int) = photoDao.deletePhoto(id)
    override suspend fun findPhotoById(id: Int) = photoDao.findPhotoById(id)
    override fun getAllPhotos() = photoDao.getAllPhotos()

    override suspend fun loadPhoto(page: Int, query: String) = api.loadPhoto(page, query)
    override suspend fun loadCuratedPhoto(page: Int) = api.loadCuratedPhoto(page)
    override suspend fun loadFeaturedCollections() = api.loadFeaturedCollections()
}