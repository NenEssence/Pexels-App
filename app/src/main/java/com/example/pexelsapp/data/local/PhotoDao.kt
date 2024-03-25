package com.example.pexelsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoDbEntity: PhotoDbEntity)

    @Update
    suspend fun updatePhoto(photoDbEntity:PhotoDbEntity)

    @Delete
    suspend fun deletePhoto(photoDbEntity: PhotoDbEntity)

    @Query("SELECT * FROM PHOTOS ORDER BY id DESC")
    fun getAllPhotos(): LiveData<List<PhotoDbEntity>>

}