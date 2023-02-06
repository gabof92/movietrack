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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateBucketDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateBucketDialogFragment : DialogFragment() {

    private var _binding: FragmentCreateBucketDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BucketListViewModel by activityViewModels {
        BucketListViewModelFactory(
            (activity?.application as MovieTrackApplication).database.bucketDao(),
        )
    }

    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
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
    }

    private fun saveButtonAction() {
        val name = binding.bucketName.text.toString()
        val description = binding.bucketDescription.text.toString()

        if (name.isBlank()) {
            binding.nameInputLayout.error = "Name can't be empty or blank"
        } else {
            binding.nameInputLayout.error = null
            lifecycleScope.launch {
                val bucket = Bucket(null, name, description)
                if(insertBucket(bucket))
                    closeDialog()
                else
                    showInsertErrorMessage()
            }
        }
    }

    private suspend fun insertBucket(bucket: Bucket) : Boolean{
        enableSaveButton(false)
        var result: Boolean
        try {
            viewModel.saveBucket(bucket)
            result = true
        } catch (e: Exception) {
            Log.e("InsertTransaction", "Error inserting bucket: $e")
            result = false
        }
        enableSaveButton(true)
        return result
    }

    private fun enableSaveButton(enable: Boolean){
        requireActivity().runOnUiThread { binding.buttonSave.isEnabled = enable }
    }

    private fun showInsertErrorMessage(){
        Toast.makeText(requireContext(), "There was an error creating the list", Toast.LENGTH_LONG).show()
    }

    private fun closeDialog(){
        requireActivity().runOnUiThread { dismiss() }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            //param1: Int
        ) =
            CreateBucketDialogFragment().apply {
                /**arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
                }*/
            }
    }
}