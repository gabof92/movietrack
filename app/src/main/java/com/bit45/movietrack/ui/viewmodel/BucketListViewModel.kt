package com.bit45.movietrack.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bit45.movietrack.data.dao.BucketDao
import com.bit45.movietrack.data.dao.BucketMovieDao
import com.bit45.movietrack.data.dao.MovieDao
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.model.BucketWithMovies
import com.bit45.movietrack.model.entity.BucketMovie
import com.bit45.movietrack.model.json.MovieJson
import com.bit45.movietrack.model.entity.Movie
import com.bit45.movietrack.model.json.Provider
import com.bit45.movietrack.model.json.ProviderLists
import com.bit45.movietrack.model.json.VideoJson
import com.bit45.movietrack.network.TmdbApi
import kotlinx.coroutines.flow.Flow

class BucketListViewModel(
    private val bucketDao: BucketDao,
    private val movieDao: MovieDao,
    private val bucketMovieDao: BucketMovieDao,
) : ViewModel() {

    private var _bucketId: Int? = null
    val bucketId get() = _bucketId
    fun setBucketId(id: Int?) {
        _bucketId = id
    }

    private var _movieId: Int? = null
    val movieId get() = _movieId
    fun setMovieId(id: Int?) {
        _movieId = id
    }


    fun getBucketList(): Flow<List<BucketWithMovies>> = bucketDao.getAllBucketsWithMovies()
    suspend fun getBucket(id: Int): Bucket = bucketDao.getBucket(id)
    fun getBucketFlow(id: Int): Flow<Bucket> = bucketDao.getBucketFlow(id)
    suspend fun insertBucket(bucket: Bucket) = bucketDao.insert(bucket)
    suspend fun updateBucket(bucket: Bucket) = bucketDao.update(bucket)
    suspend fun deleteBucket(bucket: Bucket) = bucketDao.delete(bucket)

    suspend fun insertMovieJson(m: MovieJson) = movieDao.insert(
        Movie(m.id, m.name, m.image, m.isWatched)
    )

    suspend fun updateMovieJson(m: MovieJson) = movieDao.update(
        Movie(m.id, m.name, m.image, m.isWatched)
    )

    suspend fun getMovie(id: Int): Movie = movieDao.getMovie(id)

    fun getMoviesByBucket(bucketId: Int): Flow<List<Movie>> = movieDao.getMoviesByBucket(bucketId)

    suspend fun getCurrentBucketMovie(): BucketMovie =
        bucketMovieDao.getBucketMovie(bucketId!!, movieId!!)

    suspend fun insertCurrentBucketMovie() =
        bucketMovieDao.insert(BucketMovie(bucketId!!, movieId!!))

    suspend fun deleteCurrentBucketMovie() =
        bucketMovieDao.delete(BucketMovie(bucketId!!, movieId!!))


    suspend fun getMovieFromApi(): MovieJson? {
        return try {
            TmdbApi.retrofitService.getMovie(movieId!!)
        } catch (e: Exception) {
            Log.e(null, e.stackTraceToString())
            null
        }
    }

    suspend fun mergeLocalMovieWithApi(movie: MovieJson): MovieJson {
        try {
            //Check if movie is in local database
            val localMovie = getMovie(movie.id)
            //if so, update database
            if (localMovie != null) {
                movie.isWatched = localMovie.isWatched
                updateMovieJson(movie)
            }
            //otherwise, insert movie into db
            else insertMovieJson(movie)
        } catch (e: Exception) {
            Log.e(null, e.stackTraceToString())
        }
        return movie
    }

    fun getWatchProvidersList(providers: ProviderLists?): List<Provider>{
        val list = mutableListOf<Provider>()
        //Setting purchase types so Adapter can set corresponding icon later
        providers?.streaming?.forEach { it.purchaseType = Provider.TYPE_STREAM }
        providers?.buy?.forEach { it.purchaseType = Provider.TYPE_BUY }
        providers?.rent?.forEach { it.purchaseType = Provider.TYPE_RENT }
        //Merging and sorting list
        list.addAll(providers?.streaming ?: emptyList())
        list.addAll(providers?.buy ?: emptyList())
        list.addAll(providers?.rent ?: emptyList())
        list.sortBy { it.purchaseType }
        return list
    }

    fun getTrailerVideo(movie: MovieJson): VideoJson? {
        val videos = mutableListOf<VideoJson>()
        movie.videoResponse?.videos?.let { videos.addAll(it) }
        //The filter will require an official video only if theres one in the list
        val isOfficialNecessary = (videos.find { it.isOfficial } != null)
        //Filter the list to get Official Youtube Trailers
        val youtubeTrailers = videos.filter {
            it.isOfficial == isOfficialNecessary && (it.type == VideoJson.TYPE_YOUTUBE) && (it.site == VideoJson.SITE_YOUTUBE)
        }
        //Return the best video available
        return if (youtubeTrailers.isNotEmpty())
        //get trailer with highest resolution
            youtubeTrailers.maxByOrNull { it.size }
        else
        //if all filters fail, just get any video available
            videos.firstOrNull()
    }

    // Get the ISO 3166-1 country code from the system
    fun getCountryCodeFromSystem(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.simCountryIso ?: telephonyManager.networkCountryIso

        if (countryCode.isNotEmpty()) {
            Log.d("Country Code", countryCode.uppercase())
            return countryCode.uppercase()
        }
        return ""
    }

    fun launchInternetSite(link: String, context: Context){
        val url = link.toUri().buildUpon().scheme("https").build()
        val intent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(intent)
    }
}

class BucketListViewModelFactory(
    private val bucketDao: BucketDao,
    private val movieDao: MovieDao,
    private val bucketMovieDao: BucketMovieDao,
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BucketListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return BucketListViewModel(bucketDao, movieDao, bucketMovieDao) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}