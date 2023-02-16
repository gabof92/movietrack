package com.bit45.movietrack.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import com.bit45.movietrack.databinding.BucketListItemBinding
import com.bit45.movietrack.model.BucketWithMovies


class BucketListAdapter(
    private val onItemClicked: (BucketWithMovies) -> Unit
) : ListAdapter<BucketWithMovies, BucketListAdapter.BucketViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketViewHolder {
        val viewHolder = BucketViewHolder(
            BucketListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.bindingAdapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BucketViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class BucketViewHolder(private var binding: BucketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bucketWithMovies: BucketWithMovies) {
            val bucket = bucketWithMovies.bucket
            val count = bucketWithMovies.movies.size
            binding.bucketName.text = bucket.name
            binding.bucketMovieQuantity.text = "$count Movies"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BucketWithMovies>() {
            override fun areItemsTheSame(oldItem: BucketWithMovies, newItem: BucketWithMovies): Boolean {
                return oldItem.bucket.id == newItem.bucket.id
            }

            override fun areContentsTheSame(oldItem: BucketWithMovies, newItem: BucketWithMovies): Boolean {
                val oldBucket = oldItem.bucket
                val newBucket = newItem.bucket
                val oldCount = oldItem.movies.size
                val newCount = newItem.movies.size
                val isSameBucket = oldBucket == newBucket
                val isSameCount = oldCount == newCount
                return (isSameBucket && isSameCount)
            }
        }
    }
}