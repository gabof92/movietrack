package com.bit45.movietrack.data.dao

import androidx.room.*
import com.bit45.movietrack.model.entity.Movie
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
    suspend fun getMovie(id: Int): Movie

    //Count will only return 0 or 1, which will act as boolean
    @Query("SELECT COUNT(*) FROM movie WHERE id = :id")
    suspend fun isMovieInDb(id: Int): Boolean

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