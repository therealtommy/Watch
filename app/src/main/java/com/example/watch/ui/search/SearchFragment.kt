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

        val query = arguments?.getString("query") ?: ""
        val year = arguments?.getString("year")
        if (query.isNotEmpty()) {
            viewModel.search(query, year)
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            val bundle = Bundle().apply {
                putParcelable("selectedMovie", movie)
            }
            findNavController().navigate(R.id.addFragment, bundle)
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is SearchState.Initial -> {
                        binding.tvEmpty.text = "Введите поисковый запрос"
                        binding.tvEmpty.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                    }
                    SearchState.Loading -> {
                        binding.tvEmpty.text = "Загрузка..."
                        binding.tvEmpty.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                    }
                    is SearchState.Success -> {
                        adapter.submitList(state.movies)
                        binding.tvEmpty.visibility = if (state.movies.isEmpty()) View.VISIBLE else View.GONE
                        if (state.movies.isEmpty()) binding.tvEmpty.text = "Ничего не найдено"
                    }
                    is SearchState.Error -> {
                        binding.tvEmpty.text = "Ошибка: ${state.message}"
                        binding.tvEmpty.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
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