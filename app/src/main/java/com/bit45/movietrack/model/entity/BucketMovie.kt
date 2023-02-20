package com.bit45.movietrack.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "bucket_movie",
    primaryKeys = ["id_bucket", "id_movie"],
    foreignKeys = [
            ForeignKey(entity = Bucket::class, parentColumns = ["id"], childColumns = ["id_bucket"], onDelete = CASCADE),
            ForeignKey(entity = Movie::class, parentColumns = ["id"], childColumns = ["id_movie"], onDelete = CASCADE)
    ]
)
data class BucketMovie(
    @ColumnInfo(name = "id_bucket")
    var bucket: Int,
    @ColumnInfo(name = "id_movie")
    var movie: Int,
)
