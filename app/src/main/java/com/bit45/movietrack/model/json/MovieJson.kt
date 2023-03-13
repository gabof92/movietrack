package com.bit45.movietrack.model.json

import com.bit45.movietrack.model.entity.Movie
import com.squareup.moshi.Json

data class MovieJson (

    var id: Int,

    @Json(name = "title") var name: String,

    @Json(name = "overview") var overview: String?,

    @Json(name = "poster_path") var image: String?,

    @Json(name = "vote_average") var userScore: Float,

    @Json(name = "watch/providers") var providerResponse: ProviderResponse?,

    @Json(name = "videos") var videoResponse: VideoResults?,

    @Json(name = "adult") var isAdult: Boolean,

    @Transient var isWatched: Boolean = false
)

data class MovieSearchResult(
    @Json(name = "results") val movies: List<Movie>
)
