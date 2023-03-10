package com.bit45.movietrack.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.bit45.movietrack.R
import com.bit45.movietrack.databinding.MovieListItemBinding
import com.bit45.movietrack.model.entity.Movie
import com.bit45.movietrack.network.TmdbApi

class MovieListAdapter(
    private val onItemClicked: (Movie) -> Unit
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val viewHolder = MovieViewHolder(
            MovieListItemBinding.inflate(
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

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MovieViewHolder(private var binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieName.text = movie.name
            binding.isWatchedIcon.isVisible = movie.isWatched

            val moviePoster = binding.moviePoster
            val noPosterIcon = ContextCompat.getDrawable(moviePoster.context, R.drawable.ic_movie)
            if(movie.image==null) {
                moviePoster.setImageDrawable(noPosterIcon)
                return
            }
            val url = TmdbApi.getImageUri(movie.image!!)
            moviePoster.load(url){
                placeholder(R.drawable.ic_downloading)
                error(R.drawable.ic_no_internet)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}