package com.bit45.movietrack.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bit45.movietrack.MainActivity
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.R
import com.bit45.movietrack.databinding.FragmentBucketDetailBinding
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch


/**
 * A [Fragment] subclass.
 * Use the [BucketDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BucketDetailFragment : Fragment() {

    private var _binding: FragmentBucketDetailBinding? = null
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

    private var bucket: Bucket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
        setUpMenu()

        lifecycle.coroutineScope.launch {
            viewModel.getBucketFlow(viewModel.bucketId!!).collect {
                bucket = it
                bucket?.let {
                    //Update views in this fragment
                    (activity as MainActivity).supportActionBar?.title = it.name
                    binding.bucketDescription.text = it.description
                }
            }
        }
        binding.buttonAddMovie.setOnClickListener {
            val action = BucketDetailFragmentDirections
                .actionBucketDetailFragmentToMovieSearchFragment()
            view.findNavController().navigate(action)
        }

    }

    private fun setUpMenu(){
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_bucket_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        val createBucketDialog = CreateBucketDialogFragment.newInstance()
                        createBucketDialog.show(parentFragmentManager, "create_bucket_dialog")
                        return true
                    }
                    R.id.action_delete -> {
                        val dialog = buildAlertDialog()
                        dialog.show()
                        return true
                    }
                }
                return false
            }
        },
            viewLifecycleOwner)
    }

    private fun buildAlertDialog(): AlertDialog{
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to delete ${bucket?.name}?")
        builder.setPositiveButton("Yes") { dialog, which ->
            // Action to be taken when the user clicks the "Yes" button
            bucket?.let {
                lifecycleScope.launch {
                    if (deleteBucket())
                        findNavController().navigateUp()
                    else
                        showDeleteErrorMessage()
                }
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Action to be taken when the user clicks the "No" button
            dialog.dismiss()
        }
        return builder.create()
    }

    private fun showDeleteErrorMessage() {
        Toast.makeText(
            requireContext(),
            "There was an error updating the database",
            Toast.LENGTH_LONG
        ).show()
    }

    private suspend fun deleteBucket(): Boolean {
        val result = try {
            viewModel.deleteBucket(bucket!!)
            true
        } catch (e: Exception) {
            Log.e("InsertTransaction", "Error deleting bucket: $e")
            false
        }
        return result
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BucketDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}