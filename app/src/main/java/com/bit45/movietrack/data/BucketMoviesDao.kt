package com.bit45.movietrack.data

import androidx.room.*

@Dao
interface BucketMoviesDao {
    @Insert
    suspend fun insert(bucketMovies: BucketMovies)

    @Update
    suspend fun update(bucketMovies: BucketMovies)

    @Delete
    suspend fun delete(bucketMovies: BucketMovies)

}