package com.example.pexelsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PhotoDbEntity::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
        abstract fun photoDao(): PhotoDao
}