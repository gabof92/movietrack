package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.databinding.FragmentMovieSearchBinding
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

/**
 * A [Fragment] subclass.
 * Use the [MovieSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieSearchFragment : Fragment() {

    private var _binding: FragmentMovieSearchBinding? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSearch.setOnClickListener { searchMovie() }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.movieName.text?.let {
            val title = it.toString()
            if (title.isNotEmpty()) searchMovie()
        }
    }

    private fun searchMovie() {
        val name = binding.movieName.text.toString().trim()

        if (name.isBlank()) {
            binding.nameInputLayout.error = "This can't be empty or blank"
        } else {
            binding.nameInputLayout.error = null
            lifecycleScope.launch {
                val movieList = viewModel.getMoviesFromApiSearch(name)
                movieList?.let {
                    val listFragment = binding.movieListContainer
                        .getFragment<MovieListFragment>()
                    listFragment.updateList(it)
                    return@launch
                }
                showMessage("Error connecting to online database")
            }
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
         * @return A new instance of fragment MovieSearchFragment.
         */
        @JvmStatic
        fun newInstance() =
            MovieSearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}