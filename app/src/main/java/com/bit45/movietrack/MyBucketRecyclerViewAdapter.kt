package com.bit45.movietrack

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.bit45.movietrack.placeholder.PlaceholderContent.Bucket
import com.bit45.movietrack.databinding.BucketListItemBinding

/**
 * [RecyclerView.Adapter] that can display a [Bucket].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBucketRecyclerViewAdapter(
    private val values: List<Bucket>
) : RecyclerView.Adapter<MyBucketRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BucketListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.movieQuantity.text = item.description
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: BucketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.bucketName
        val movieQuantity: TextView = binding.bucketMovieQuantity

    }

}