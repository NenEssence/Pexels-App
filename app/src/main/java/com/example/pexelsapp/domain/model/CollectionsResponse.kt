package com.example.pexelsapp.domain.model

data class CollectionsResponse(
    val collections: List<FeaturedCollection>,
    val page: Int,
    val per_page: Int,
)