package com.bit45.movietrack.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bit45.movietrack.MovieTrackApplication
import com.bit45.movietrack.model.entity.Bucket
import com.bit45.movietrack.databinding.FragmentCreateBucketDialogBinding
import com.bit45.movietrack.ui.viewmodel.BucketListViewModel
import com.bit45.movietrack.ui.viewmodel.BucketListViewModelFactory
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CreateBucketDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateBucketDialogFragment : DialogFragment() {

    private var _binding: FragmentCreateBucketDialogBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
            (activity?.application as MovieTrackApplication).database.movieDao(),
            (activity?.application as MovieTrackApplication).database.bucketMovieDao()
        )
    }

    private var bucket = Bucket(null, "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBucketDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener { saveButtonAction() }


        viewModel.bucketId?.let {
            lifecycleScope.launch {
                bucket = viewModel.getBucket(it)
                requireActivity().runOnUiThread {
                    binding.nameInputLayout.editText?.setText(bucket.name)
                    binding.descInputLayout.editText?.setText(bucket.description)
                }
            }
        }
    }

    private fun saveButtonAction() {
        bucket.name = binding.bucketName.text.toString()
        bucket.description = binding.bucketDescription.text.toString()

        if (bucket.name.isBlank()) {
            binding.nameInputLayout.error = "Name can't be empty or blank"
        } else {
            binding.nameInputLayout.error = null
            lifecycleScope.launch {
                if (insertOrUpdate())
                    closeDialog()
                else
                    showInsertErrorMessage("There was an error updating the database")
            }
        }
    }

    private suspend fun insertOrUpdate(): Boolean {
        setButtonEnabled(false)
        val result = try {
            if (bucket.id == null)
                viewModel.insertBucket(bucket)
            else
                viewModel.updateBucket(bucket)
            true
        } catch (e: Exception) {
            Log.e("InsertTransaction", "Error inserting/updating bucket: $e")
            false
        }
        setButtonEnabled(true)
        return result
    }

    private fun setButtonEnabled(enable: Boolean) {
        requireActivity().runOnUiThread { binding.buttonSave.isEnabled = enable }
    }

    private fun showInsertErrorMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun closeDialog() {
        requireActivity().runOnUiThread { dismiss() }
    }

    companion object {
        @JvmStatic
        fun newInstance(
        ) =
            CreateBucketDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}