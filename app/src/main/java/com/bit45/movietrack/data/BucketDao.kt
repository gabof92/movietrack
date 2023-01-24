package com.bit45.movietrack.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BucketDao {
    @Insert
    suspend fun insert(bucket: Bucket)

    @Update
    suspend fun update(bucket: Bucket)

    @Delete
    suspend fun delete(bucket: Bucket)

    @Query("SELECT * FROM bucket WHERE id = :id")
    fun getBucket(id: Int): Flow<Bucket>

    @Query("SELECT * FROM bucket")
    fun getAllBuckets(): Flow<List<Bucket>>


    @Query(
        "SELECT COUNT(*) " +
                "FROM bucket_movie " +
                "INNER JOIN bucket " +
                "ON bucket_movie.id_bucket = bucket.id " +
                "INNER JOIN movie " +
                "ON bucket_movie.id_movie = movie.id " +
                "WHERE id_bucket = :id"
    )
    fun getMovieCountByBucket(id: Int): Flow<Int>

    @Query(
        "SELECT bucket.* " +
                "FROM bucket_movie " +
                "INNER JOIN bucket " +
                "ON bucket_movie.id_bucket = bucket.id " +
                "INNER JOIN movie " +
                "ON bucket_movie.id_movie = movie.id " +
                "WHERE id_movie = :id"
    )
    fun getBucketsByMovie(id: Int): Flow<List<Bucket>>
}