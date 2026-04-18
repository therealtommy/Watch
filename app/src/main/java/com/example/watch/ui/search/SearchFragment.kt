package com.example.watch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watch.R
import com.example.watch.databinding.FragmentSearchBinding
import com.example.watch.ui.search.SearchIntent.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(requireContext()) }
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        observeEffect()

        val query = arguments?.getString("query") ?: ""
        val year = arguments?.getString("year")
        if (query.isNotEmpty()) {
            viewModel.processIntent(Search(query, year))
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            viewModel.processIntent(SelectMovie(movie))
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (_binding != null) {
                    adapter.submitList(state.movies)
                    when {
                        state.isLoading -> {
                            binding.tvEmpty.text = "Загрузка..."
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                        state.error != null -> {
                            binding.tvEmpty.text = "Ошибка: ${state.error}"
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                        state.movies.isEmpty() && !state.isLoading -> {
                            binding.tvEmpty.text = "Ничего не найдено"
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                        else -> binding.tvEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is SearchEffect.NavigateToAdd -> {
                        val bundle = Bundle().apply {
                            putParcelable("selectedMovie", effect.movie)
                        }
                        findNavController().navigate(R.id.addFragment, bundle)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}