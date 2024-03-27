package com.example.pexelsapp.data.remote.model

data class PexelsApiEntity(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)