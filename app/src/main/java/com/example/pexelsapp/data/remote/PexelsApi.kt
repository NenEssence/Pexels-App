package com.example.pexelsapp.data.remote

import com.example.pexelsapp.data.remote.model.CollectionsResponse
import com.example.pexelsapp.data.remote.model.PexelsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PexelsApi {
    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("search")
    suspend fun getPhoto(
        @Query("query") query: String,
        @Query("per_page") perpage: Int = 30
    ): Response<PexelsApiResponse>

    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("search/")
    suspend fun loadMorePhoto(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("per_page") perpage: Int = 30
    ): Response<PexelsApiResponse>

    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("collections/featured")
    suspend fun getFeaturedCollections(
        @Query("page") page: Int = 1,
        @Query("per_page") perpage: Int = 7
    ): Response<CollectionsResponse>
}