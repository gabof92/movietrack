package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.bit45.movietrack.ui.adapter.BucketListAdapter
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.databinding.FragmentBucketListBinding
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class BucketListFragment : Fragment() {

    private var _binding: FragmentBucketListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
            (activity?.application as MovieTrackApplication).database.movieDao(),
            (activity?.application as MovieTrackApplication).database.bucketMovieDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBucketListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        //The adapter receives the action that every item will do when clicked
        val adapter = BucketListAdapter { openBucketDetail(it.bucket.id!!, view) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        //Assign a list to the adapter (from database, so running in coroutine)
        lifecycle.coroutineScope.launch {
            viewModel.getBucketList().collect() { adapter.submitList(it) }
        }

        binding.createBucketButton.setOnClickListener {
            viewModel.setBucketId(null)
            val createBucketDialog = CreateBucketDialogFragment.newInstance()
            createBucketDialog.show(parentFragmentManager, "create_bucket_dialog")
        }
    }

    private fun openBucketDetail(bucketId: Int, view: View){
        viewModel.setBucketId(bucketId)
        val action = BucketListFragmentDirections
            .actionBucketsFragmentToBucketDetailFragment()
        view.findNavController().navigate(action)
    }

}