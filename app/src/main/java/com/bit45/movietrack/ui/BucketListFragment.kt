package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.bit45.movietrack.adapter.BucketRecyclerViewAdapter
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.databinding.BucketListFragmentBinding
import com.bit45.movietrack.viewmodel.BucketListViewModel
import com.bit45.movietrack.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class BucketListFragment : Fragment() {

    private var _binding: BucketListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BucketListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the adapter & layout manager
        val recyclerView = binding.recyclerView
        val adapter = BucketRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        //Assign a list to the adapter (from database, so running in coroutine)
        lifecycle.coroutineScope.launch {
            viewModel.getBuckets().collect()
            {
                adapter.submitList(it)
            }
        }
    }

}