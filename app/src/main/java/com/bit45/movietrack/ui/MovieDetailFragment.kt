package com.bit45.movietrack.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.bit45.movietrack.MainActivity
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.R
import com.bit45.movietrack.databinding.FragmentMovieDetailBinding
import com.bit45.movietrack.model.json.MovieJson
import com.bit45.movietrack.network.TmdbApi
import com.bit45.movietrack.ui.adapter.ProviderListAdapter
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"

/**
 * A [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    //private var param1: String? = null

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
            (activity?.application as MovieTrackApplication).database.movieDao(),
            (activity?.application as MovieTrackApplication).database.bucketMovieDao(),
        )
    }

    private var isInBucket: Boolean = false
    private var movie: MovieJson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.movieId == null) return

        lifecycleScope.launch {

            /** Connecting to TMDB */
            movie = viewModel.getMovieFromApi()
            if (movie == null) {
                //handle TMDB connection fail
                (activity as MainActivity)
                    .supportActionBar?.title = "Internet Connection Error"
                setButtonsEnabled(false)
                showMessage("Error connecting to the online database")
            }

            movie?.let {

                //retrieve local db info or add movie to local
                viewModel.mergeLocalMovieWithApi(it)

                /** Populating non-interactive views */
                loadPosterImage()
                //set fragment title
                (activity as MainActivity)
                    .supportActionBar?.title = it.name
                //set overview
                binding.overviewText.text = it.overview ?: ""
                //set rating bar rating
                binding.userScoreBar.rating = (it.userScore / 2)

                /** Setting up "Add Movie" Button */
                if (!retrieveBucketMovieRelationship())
                    binding.addMovieButton.isEnabled = false
                updateAddButtonUI()
                binding.addMovieButton.setOnClickListener {
                    lifecycleScope.launch { addOrRemoveMovieFromBucket() }
                }

                /** Setting up "switchWatched" view */
                binding.switchWatched.isChecked = it.isWatched
                //update database when switch is toggled
                binding.switchWatched.setOnCheckedChangeListener { switchButton, isChecked ->
                    lifecycleScope.launch {
                        movie?.isWatched = isChecked
                        if (!updateLocalDb()) {
                            movie?.isWatched = !isChecked
                            switchButton.isChecked = !isChecked
                        }
                    }
                }

                /** Setting up "Where to Watch" list */
                //Obtain provider lists based on current country
                val countryCode = viewModel.getCountryCodeFromSystem(requireContext())
                val providers = it.providerResponse?.results?.get(countryCode)
                //adapter receives link to TMDB watchproviders website
                val adapter = ProviderListAdapter(providers?.link)
                //Get sorted provider list ready for adapter
                val list = viewModel.getWatchProvidersList(providers)
                //set adapter and layout manager
                val recyclerView = binding.providerList
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = adapter
                adapter.submitList(list)

                /** Setting up "Trailer" button */
                val bestVideo = viewModel.getTrailerVideo(it)
                binding.trailerButton.isEnabled = (bestVideo != null)
                binding.trailerButton.setOnClickListener{
                    bestVideo?.let {
                        val youtubeLink: String = "https://www.youtube.com/watch?v="+it.key
                        viewModel.launchInternetSite(youtubeLink, requireContext())
                    }
                }
            }

        }
    }

    private suspend fun updateLocalDb(): Boolean {
        if (movie == null) return false
        return try {
            viewModel.updateMovieJson(movie!!)
            true
        } catch (e: Exception) {
            showMessage("There was an error updating the database")
            false
        }
    }

    private suspend fun retrieveBucketMovieRelationship(): Boolean {
        return try {
            val relation = viewModel.getCurrentBucketMovie()
            isInBucket = (relation != null)
            true
        } catch (e: Exception) {
            Log.e(null, e.stackTraceToString())
            showMessage("Error retrieving movie relationtship to list")
            false
        }
    }

    private suspend fun addOrRemoveMovieFromBucket(): Boolean {
        return try {
            if (isInBucket)
                viewModel.deleteCurrentBucketMovie()
            else
                viewModel.insertCurrentBucketMovie()

            isInBucket = !isInBucket
            updateAddButtonUI()
            true
        } catch (e: Exception) {
            Log.e(null, e.stackTraceToString())
            showMessage("Error adding/removing movie from list")
            false
        }
    }

    private fun loadPosterImage() {
        movie?.let {
            val moviePoster = binding.imagePoster
            //use default icon if movie doesn't have a poster
            if (it.image == null) {
                val noPosterIcon =
                    ContextCompat.getDrawable(moviePoster.context, R.drawable.ic_movie)
                moviePoster.setImageDrawable(noPosterIcon)
            }
            //load poster into imageview from movie path
            else {
                val url = TmdbApi.getImageUri(it.image!!)
                moviePoster.load(url) {
                    placeholder(R.drawable.ic_downloading)
                    error(R.drawable.ic_no_internet)
                }
            }
        }
    }

    private fun setButtonsEnabled(enable: Boolean) {
        binding.apply {
            trailerButton.isEnabled = enable
            addMovieButton.isEnabled = enable
            switchWatched.isEnabled = enable
        }
    }

    private fun updateAddButtonUI() {
        //todo use style resource instead
        if (!isInBucket) {
            binding.addMovieButton.setBackgroundColor(resources.getColor(R.color.purple_700))
            binding.addMovieButton.text = "Add to List"
        } else {
            binding.addMovieButton.setBackgroundColor(Color.RED)
            binding.addMovieButton.text = "Remove From List"
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(
            //param1: String
        ) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                }
            }
    }
}