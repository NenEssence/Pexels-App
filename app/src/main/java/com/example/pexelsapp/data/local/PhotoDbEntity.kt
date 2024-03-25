package com.example.pexelsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val photographer: String,
    val utl: String
)
