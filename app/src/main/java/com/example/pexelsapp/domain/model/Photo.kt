package com.example.pexelsapp.domain.model

data class Photo(
    val alt: String,
    val avg_color: String,
    val height: Int,
    val id: Int,
    val photographer: String,
    val src: Src,
    val url: String,
    val width: Int
)