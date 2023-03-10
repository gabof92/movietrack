package com.bit45.movietrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bit45.movietrack.data.dao.BucketDao
import com.bit45.movietrack.data.dao.BucketMovieDao
import com.bit45.movietrack.data.dao.MovieDao
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.model.entity.BucketMovie
import com.bit45.movietrack.model.entity.Movie

@Database(entities = [Bucket::class, Movie::class, BucketMovie::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun bucketDao(): BucketDao
    abstract fun movieDao(): MovieDao
    abstract fun bucketMovieDao(): BucketMovieDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }

}