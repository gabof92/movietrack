package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.databinding.FragmentMovieListBinding
import com.bit45.movietrack.model.entity.Movie
import com.bit45.movietrack.ui.adapter.MovieListAdapter
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
            (activity?.application as MovieTrackApplication).database.movieDao(),
            (activity?.application as MovieTrackApplication).database.bucketMovieDao()
        )
    }

    private var columnCount = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.list
        //Create adapter receives the action that every item will do when clicked
        val adapter = MovieListAdapter {
            viewModel.setMovieId(it.id)
            val action = BucketDetailFragmentDirections
                .actionBucketDetailFragmentToMovieDetailFragment()
            binding.root.findNavController().navigate(action)
        }
        //set adapter and layout manager
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, columnCount)


        //Get movies as Flow object from Room database and update list when changes are made
        lifecycle.coroutineScope.launch {
            viewModel.getMoviesByBucket(viewModel.bucketId!!).collect {
                adapter.submitList(it)
            }
        }
    }

}