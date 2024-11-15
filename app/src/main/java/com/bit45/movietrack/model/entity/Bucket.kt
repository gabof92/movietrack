package com.bit45.movietrack.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bucket")
data class Bucket(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var description: String
)