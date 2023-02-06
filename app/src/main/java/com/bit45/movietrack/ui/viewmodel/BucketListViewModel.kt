package com.bit45.movietrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bit45.movietrack.data.dao.BucketDao
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.model.BucketWithMovies
import kotlinx.coroutines.flow.Flow

class BucketListViewModel(
    private val bucketDao: BucketDao,
) : ViewModel() {

    fun getBuckets(): Flow<List<BucketWithMovies>> = bucketDao.getAllBucketsWithMovies()

    suspend fun saveBucket(bucket: Bucket) = bucketDao.insert(bucket)

}

class BucketListViewModelFactory(
    private val bucketDao: BucketDao,
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BucketListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return BucketListViewModel(bucketDao) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}