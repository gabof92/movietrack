package com.bit45.movietrack.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bit45.movietrack.R
import com.bit45.movietrack.databinding.ProviderListItemBinding
import com.bit45.movietrack.model.json.Provider
import com.bit45.movietrack.network.TmdbApi

class ProviderListAdapter(
    private val justWatchLink: String?
) : ListAdapter<Provider, ProviderListAdapter.ProviderViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {

        val viewHolder = ProviderViewHolder(
            ProviderListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val context = it.context
            justWatchLink?.let {
                val url = it.toUri().buildUpon().scheme("https").build()
                val intent = Intent(Intent.ACTION_VIEW, url)
                context.startActivity(intent)
            }
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ProviderViewHolder(private var binding: ProviderListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(provider: Provider) {
            binding.providerName.text = provider.name

            //Setting logo image
            val providerImage = binding.providerLogo
            if (provider.logo == null) {
                //Assign default icon if API doesn't have logo for provider
                val noLogoDrawable =
                    ContextCompat.getDrawable(providerImage.context, R.drawable.ic_tv_connected)
                providerImage.setImageDrawable(noLogoDrawable)
            } else {
                //Load logo from API
                val url = TmdbApi.getImageUri(provider.logo)
                providerImage.load(url) {
                    placeholder(R.drawable.ic_downloading)
                    error(R.drawable.ic_broken)
                }
            }

            //Setting purchase type icon
            val purchaseTypeImage = binding.providerTypeIcon
            val iconResourceId = when(provider.purchaseType){
                Provider.TYPE_RENT -> R.drawable.ic_rent
                Provider.TYPE_BUY -> R.drawable.ic_buy
                Provider.TYPE_STREAM -> R.drawable.ic_stream
                else -> R.drawable.ic_broken
            }
            val purchaseTypeDrawable =
                ContextCompat.getDrawable(providerImage.context, iconResourceId)
            purchaseTypeImage.setImageDrawable(purchaseTypeDrawable)

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Provider>() {
            override fun areItemsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Provider, newItem: Provider): Boolean {
                return oldItem == newItem
            }
        }
    }

}