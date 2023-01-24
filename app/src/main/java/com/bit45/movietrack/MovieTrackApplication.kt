package com.bit45.movietrack

import android.app.Application
import com.bit45.movietrack.data.AppDatabase

class MovieTrackApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}