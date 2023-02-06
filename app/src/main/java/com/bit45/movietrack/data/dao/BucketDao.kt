package com.bit45.movietrack.data.dao

import androidx.room.*
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.model.BucketWithMovies
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

    /** Since @Relation fields are queried separately. Need to run all queries
     * in a single transaction to receive consistent results.*/
    @Transaction
    @Query("SELECT * FROM bucket ORDER BY id DESC")
    fun getAllBucketsWithMovies(): Flow<List<BucketWithMovies>>

    @Query("SELECT * FROM bucket")
    fun getAllBucketsFlow(): Flow<List<Bucket>>

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