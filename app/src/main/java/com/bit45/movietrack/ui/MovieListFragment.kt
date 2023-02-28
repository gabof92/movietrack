package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bit45.movietrack.databinding.FragmentMovieListBinding
import com.bit45.movietrack.model.entity.Movie
import com.bit45.movietrack.ui.adapter.MovieListAdapter

/**
 * A fragment representing a list of Items.
 */
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

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
        //The adapter receives the action that every item will do when clicked
        recyclerView.adapter = MovieListAdapter {
            //TODO pass bucket id
            val action = BucketDetailFragmentDirections
                .actionBucketDetailFragmentToMovieDetailFragment(0, it.id)
            binding.root.findNavController().navigate(action)
        }

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
    }


    fun submitList(movies: List<Movie>) {
        val adapter = binding.list.adapter as MovieListAdapter
        adapter.submitList(movies)
    }

}