package com.bit45.movietrack.model.json

import com.squareup.moshi.Json

data class VideoJson(
    val id: String,
    val name: String,
    val site: String,
    val type: String,
    val key: String,
    val size: Int,
    @Json(name = "official") val isOfficial: Boolean,
){

    companion object {
        const val SITE_YOUTUBE = "YouTube"
        const val TYPE_YOUTUBE = "Trailer"
    }
}

data class VideoResults(
    @Json(name = "results") val videos: List<VideoJson>,
)
