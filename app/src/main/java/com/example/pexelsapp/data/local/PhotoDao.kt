package com.example.pexelsapp.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pexelsapp.domain.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoDbEntity: PhotoDbEntity)

    @Query("SELECT * FROM PHOTOS WHERE id=:id")
    suspend fun findPhotoById(id: Int): PhotoDbEntity?

    @Query("DELETE FROM PHOTOS WHERE id = :id")
    suspend fun deletePhoto(id: Int)
    @Query("SELECT * FROM PHOTOS")
    fun getAllPhotos(): Flow<List<PhotoDbEntity>>

}