package com.bit45.movietrack.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bucket_movie")
data class BucketMovies(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "id_movie")
    val movie: Int,
    @ColumnInfo(name = "id_bucket")
    val bucket: Int,
)
