package com.example.pexelsapp.data.remote

import com.example.pexelsapp.domain.model.CollectionsResponse
import com.example.pexelsapp.domain.model.PexelsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PexelsApi {
    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("search/")
    suspend fun loadPhoto(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("per_page") perpage: Int = 30
    ): Response<PexelsApiResponse>

    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("collections/featured")
    suspend fun loadFeaturedCollections(
        @Query("page") page: Int = 1,
        @Query("per_page") perpage: Int = 7
    ): Response<CollectionsResponse>
    @Headers("Authorization: S3aEK0YKhQmibMS9O8oK9W79a38P11Y0MbQKjhyXtZTCzJ1AnMXWBIlz")
    @GET("curated")
    suspend fun loadCuratedPhoto(
        @Query("page") page: Int,
        @Query("per_page") perpage: Int = 30
    ): Response<PexelsApiResponse>
}