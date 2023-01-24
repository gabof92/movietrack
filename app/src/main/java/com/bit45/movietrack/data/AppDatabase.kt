package com.bit45.movietrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(
    Bucket::class,
    Movie::class,
    BucketMovies::class),
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun bucketDao(): BucketDao
    abstract fun movieDao(): MovieDao
    abstract fun bucketMoviesDAO(): BucketMoviesDao

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