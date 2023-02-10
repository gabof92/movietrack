package com.bit45.movietrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit45.movietrack.R

// the fragment initialization parameters
private const val ARG_ID = "bucketId"

/**
 * A [Fragment] subclass.
 * Use the [BucketDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BucketDetailFragment : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bucket_detail, container, false)
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