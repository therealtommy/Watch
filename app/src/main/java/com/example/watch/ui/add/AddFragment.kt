package com.example.watch.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.watch.R
import com.example.watch.databinding.FragmentAddBinding
import com.example.watch.model.OmdbMovie
import com.example.watch.ui.add.AddIntent.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddViewModel by viewModels { AddViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEffect()

        val selectedMovie = arguments?.getParcelable<OmdbMovie>("selectedMovie")
        viewModel.processIntent(SetSelectedMovie(selectedMovie))

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
                val bundle = Bundle().apply {
                    putString("query", query)
                    putString("year", year.ifEmpty { null })
                }
                findNavController().navigate(R.id.action_add_to_search, bundle)
            }
        }

        binding.btnAdd.setOnClickListener {
            viewModel.processIntent(AddToWatchlist)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                binding.btnAdd.isEnabled = !state.isAdding && state.selectedMovie != null
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    AddEffect.MovieAdded -> findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}