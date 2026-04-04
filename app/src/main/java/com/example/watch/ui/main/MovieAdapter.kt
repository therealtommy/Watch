package com.example.watch.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watch.R
import com.example.watch.databinding.ItemMovieBinding
import com.example.watch.model.Movie

class MovieAdapter(
    private val onCheckChanged: (String, Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    var items: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selectedIds: Set<String> = emptySet()
        set(value) {
            val old = field
            field = value
            // Обновляем только те элементы, у которых изменился статус
            val changedPositions = mutableListOf<Int>()
            for (i in items.indices) {
                val id = items[i].imdbID
                val wasSelected = old.contains(id)
                val isSelected = value.contains(id)
                if (wasSelected != isSelected) {
                    changedPositions.add(i)
                }
            }
            changedPositions.forEach { position ->
                notifyItemChanged(position)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie, selectedIds.contains(movie.imdbID), onCheckChanged)
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, isChecked: Boolean, onCheckChanged: (String, Boolean) -> Unit) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year
            binding.checkBox.isChecked = isChecked
            Glide.with(binding.root).load(movie.posterUrl).into(binding.ivPoster)
            binding.checkBox.setOnCheckedChangeListener { _, checked ->
                onCheckChanged(movie.imdbID, checked)
            }
        }
    }
}