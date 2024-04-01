package com.example.pexelsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pexelsapp.domain.model.Photo
import com.example.pexelsapp.domain.model.Src

@Entity(tableName = "photos")
data class PhotoDbEntity(
    @PrimaryKey
    val id: Int,
    val alt: String,
    val avg_color: String,
    val height: Int,
    val photographer: String,
    val portrait: String,
    val url: String,
    val width: Int
) {
    fun toPhoto(): Photo {
        val alt: String = this.alt
        val avg_color: String = this.avg_color
        val height: Int = this.height
        val id: Int = this.id
        val photographer: String = this.photographer
        val src: Src = Src(this.portrait)
        val url: String = this.url
        val width: Int = this.width
        return Photo(alt, avg_color, height, id, photographer, src, url, width)
    }
}
