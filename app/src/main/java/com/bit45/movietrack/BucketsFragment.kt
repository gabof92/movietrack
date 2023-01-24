package com.bit45.movietrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit45.movietrack.databinding.BucketListFragmentBinding
import com.bit45.movietrack.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class BucketsFragment : Fragment() {


    private var _binding: BucketListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyBucketRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
    }

}