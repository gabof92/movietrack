package com.bit45.movietrack.network

import com.bit45.movietrack.model.entity.Movie
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "26f083bfabeb7d76eec1aac5a930e3c8"

const val BASE_URL_API =
    "https://api.themoviedb.org/3/"

const val BASE_URL_IMG =
    "https://image.tmdb.org/t/p/w92"

//Creating Moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Creating Retrofit Object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_API)
    .build()

interface TmdbApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Movie
}

object TmdbApi {
    val retrofitService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}