package com.bit45.movietrack.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.model.entity.BucketMovie
import com.bit45.movietrack.model.entity.Movie

data class BucketWithMovies(

    @Embedded val bucket: Bucket,

    @Relation(
        //bucket.id
        parentColumn = "id",
        //movie.id
        entityColumn = "id",
        associateBy = Junction(
            BucketMovie::class,
            parentColumn = "id_bucket",
            entityColumn = "id_movie"
        )
    )
    val movies: List<Movie>
)
