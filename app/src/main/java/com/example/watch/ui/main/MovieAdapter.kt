package com.example.watch.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watch.databinding.ItemMovieBinding
import com.example.watch.model.Movie

class MovieAdapter(
    private val onCheckChanged: (String, Boolean) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(MovieDiffCallback()) {

    private var selectedIds: Set<String> = emptySet()

    fun updateSelectedIds(newSelectedIds: Set<String>) {
        val old = selectedIds
        selectedIds = newSelectedIds
        // Обновляем только те позиции, где статус выбора изменился
        for (i in 0 until currentList.size) {
            val id = currentList[i].imdbID
            val wasSelected = old.contains(id)
            val isSelected = newSelectedIds.contains(id)
            if (wasSelected != isSelected) {
                notifyItemChanged(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onCheckChanged)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, selectedIds.contains(movie.imdbID))
    }

    class ViewHolder(
        private val binding: ItemMovieBinding,
        private val onCheckChanged: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, isChecked: Boolean) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year
            // Сбрасываем слушатель, чтобы избежать лишних вызовов
            binding.checkBox.setOnCheckedChangeListener(null)
            binding.checkBox.isChecked = isChecked
            binding.checkBox.setOnCheckedChangeListener { _, checked ->
                onCheckChanged(movie.imdbID, checked)
            }
            Glide.with(binding.root).load(movie.posterUrl).into(binding.ivPoster)
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.imdbID == newItem.imdbID

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}