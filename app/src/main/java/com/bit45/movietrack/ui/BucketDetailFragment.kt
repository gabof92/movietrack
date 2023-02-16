package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.bit45.movietrack.MainActivity
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.databinding.FragmentBucketDetailBinding
import com.bit45.movietrack.ui.viewmodel.BucketDetailViewModel
import com.bit45.movietrack.ui.viewmodel.BucketDetailViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// the fragment initialization parameters
private const val ARG_ID = "bucketId"

/**
 * A [Fragment] subclass.
 * Use the [BucketDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BucketDetailFragment : Fragment() {

    private var _binding: FragmentBucketDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BucketDetailViewModel by activityViewModels {
        BucketDetailViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
        )
    }

    private var bucketId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bucketId = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBucketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setBucket(bucketId!!).onEach {
            (activity as MainActivity).supportActionBar?.title = it.bucket.name
            binding.bucketDescription.text = it.bucket.description
            val movieFragment = childFragmentManager
                .findFragmentById(binding.movieListContainer.id)
            as MovieListFragment
            movieFragment.submitList(it.movies)
        }.launchIn(lifecycle.coroutineScope)
    }

    companion object {
        @JvmStatic
        fun newInstance(bucketId: Int) =
            BucketDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, bucketId)
                }
            }
    }
}