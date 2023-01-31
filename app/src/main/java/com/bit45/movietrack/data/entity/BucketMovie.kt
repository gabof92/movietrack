package com.bit45.movietrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "bucket_movie",
    primaryKeys = ["id_bucket", "id_movie"]
)
data class BucketMovie(
    @ColumnInfo(name = "id_bucket")
    val bucket: Int,
    @ColumnInfo(name = "id_movie")
    val movie: Int,
)
