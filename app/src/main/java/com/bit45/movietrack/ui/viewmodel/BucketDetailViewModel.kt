package com.bit45.movietrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bit45.movietrack.data.dao.BucketDao
import com.bit45.movietrack.model.BucketWithMovies
import com.bit45.movietrack.model.entity.Bucket
import kotlinx.coroutines.flow.Flow

class BucketDetailViewModel(
    private val bucketDao: BucketDao,
) : ViewModel() {

    lateinit var bucket: Flow<BucketWithMovies>

    fun setBucket(id: Int): Flow<BucketWithMovies>{
        bucket = bucketDao.getBucketWithMovies(id)
        return bucket
    }

    suspend fun deleteBucket(bucket: Bucket) = bucketDao.delete(bucket)

}

class BucketDetailViewModelFactory(
    private val bucketDao: BucketDao,
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BucketDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return BucketDetailViewModel(bucketDao) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}