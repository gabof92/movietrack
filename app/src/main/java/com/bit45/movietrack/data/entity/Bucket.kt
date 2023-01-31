package com.bit45.movietrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bucket")
data class Bucket(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val description: String
)