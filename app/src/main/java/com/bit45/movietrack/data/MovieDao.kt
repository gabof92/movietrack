package com.bit45.movietrack.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Flow<Movie>

    @Query("SELECT * FROM movie")
    fun getAllMovies(): Flow<List<Movie>>

    @Query(
        "SELECT movie.* " +
                "FROM bucket_movie " +
                "INNER JOIN bucket " +
                "ON bucket_movie.id_bucket = bucket.id " +
                "INNER JOIN movie " +
                "ON bucket_movie.id_movie = movie.id " +
                "WHERE id_bucket = :id"
    )
    fun getMoviesByBucket(id: Int): Flow<List<Movie>>
}