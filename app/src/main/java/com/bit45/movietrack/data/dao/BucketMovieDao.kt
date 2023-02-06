package com.bit45.movietrack.data.dao

import androidx.room.*
import com.bit45.movietrack.model.entity.BucketMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface BucketMovieDao {
    @Insert
    suspend fun insert(bucketMovie: BucketMovie)

    @Update
    suspend fun update(bucketMovie: BucketMovie)

    @Delete
    suspend fun delete(bucketMovie: BucketMovie)

    @Query("SELECT * FROM bucket_movie")
    fun getAllBucketMoviesFlow(): Flow<List<BucketMovie>>

}