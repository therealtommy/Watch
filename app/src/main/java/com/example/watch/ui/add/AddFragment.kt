package com.example.watch.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.watch.R
import com.example.watch.data.MovieRepository
import com.example.watch.databinding.FragmentAddBinding
import com.example.watch.db.MovieDatabase
import com.example.watch.model.Movie
import com.example.watch.model.OmdbMovie
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: MovieRepository
    private var selectedMovie: OmdbMovie? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = MovieDatabase.getInstance(requireContext()).movieDao()
        repository = MovieRepository(dao)
        selectedMovie = arguments?.getParcelable<OmdbMovie>("selectedMovie")
        selectedMovie?.let { movie ->
            binding.etQuery.setText(movie.title)
            binding.etYear.setText(movie.year)
            binding.ivPoster.visibility = View.VISIBLE
            Glide.with(this).load(movie.posterUrl).into(binding.ivPoster)
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etQuery.text.toString().trim()
            val year = binding.etYear.text.toString().trim()
            if (query.isNotEmpty()) {
                // Передаём параметры поиска через Bundle
                val bundle = Bundle().apply {
                    putString("query", query)
                    putString("year", year.ifEmpty { null })
                }
                findNavController().navigate(R.id.action_add_to_search, bundle)
            }
        }

        binding.btnAdd.setOnClickListener {
            selectedMovie?.let { movie ->
                val movieEntity = Movie(
                    imdbID = movie.imdbID,
                    title = movie.title,
                    year = movie.year,
                    posterUrl = movie.posterUrl,
                    genre = movie.genre
                )
                lifecycleScope.launch {
                    repository.addMovie(movieEntity)
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}