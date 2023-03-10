package com.bit45.movietrack.model.json

import com.squareup.moshi.Json

data class MovieJson (

    var id: Int,

    @Json(name = "original_title") var name: String,

    @Json(name = "overview") var overview: String?,

    @Json(name = "poster_path") var image: String?,

    @Json(name = "vote_average") var userScore: Float,

    @Json(name = "watch/providers") var providerResponse: ProviderResponse?,

    @Json(name = "videos") var videoResponse: VideoResults?,

    @Transient var isWatched: Boolean = false
)

