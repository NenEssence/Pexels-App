package com.example.pexelsapp.di

import android.content.Context
import androidx.room.Room
import com.example.pexelsapp.data.PhotoRepositoryImpl
import com.example.pexelsapp.data.local.PhotoDao
import com.example.pexelsapp.data.local.PhotoDatabase
import com.example.pexelsapp.data.remote.PexelsApi
import com.example.pexelsapp.domain.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideRepository(
        photoDao: PhotoDao,
        pexelsApi: PexelsApi
    ): PhotoRepository = PhotoRepositoryImpl(photoDao, pexelsApi)

    @Provides
    fun providesPhotoDao(photoDatabase: PhotoDatabase): PhotoDao = photoDatabase.photoDao()

    @Provides
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase =
        Room.databaseBuilder(context, PhotoDatabase::class.java, "database.db").build()

    @Provides
    fun providesPexelsApi(retrofit: Retrofit): PexelsApi = retrofit.create(PexelsApi::class.java)


    @Provides
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}