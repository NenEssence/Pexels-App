package com.example.pexelsapp.data.remote.model

data class CollectionsResponse(
    val collections: List<FeaturedCollection>,
    val page: Int,
    val per_page: Int,
)