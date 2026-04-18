package com.example.watch.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watch.databinding.ItemSearchResultBinding
import com.example.watch.domain.model.Movie

class SearchAdapter(private val onItemClick: (Movie) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var items: List<Movie> = emptyList()

    fun submitList(list: List<Movie>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: ItemSearchResultBinding, private val onItemClick: (Movie) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year
            binding.tvType.text = "Type: ${movie.type ?: "N/A"}"
            Glide.with(binding.root).load(movie.posterUrl).into(binding.ivPoster)
            itemView.setOnClickListener { onItemClick(movie) }
        }
    }
}