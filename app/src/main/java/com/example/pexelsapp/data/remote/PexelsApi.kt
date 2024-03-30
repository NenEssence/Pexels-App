package com.example.pexelsapp.data.remote

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
    suspend fun loadNextPage(
        @Query("page") page: Int,
        @Query("per_page") perpage: Int = 30,
        @Query("query") query: String
        )

}