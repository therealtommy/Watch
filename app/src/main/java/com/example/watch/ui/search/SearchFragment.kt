package com.example.watch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watch.BuildConfig
import com.example.watch.R
import com.example.watch.databinding.FragmentSearchBinding
import com.example.watch.model.OmdbMovie
import com.example.watch.network.RetrofitClient
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val query = arguments?.getString("query") ?: ""
        val year = arguments?.getString("year")
        performSearch(query, year)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            // Передаём выбранный фильм на AddFragment через Bundle
            val bundle = Bundle().apply {
                putParcelable("selectedMovie", movie)
            }
            findNavController().navigate(R.id.addFragment, bundle)
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun performSearch(query: String, year: String?) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchMovies("174065d5", query, year)
                if (response.response == "True") {
                    adapter.submitList(response.search ?: emptyList())
                    binding.tvEmpty.visibility = View.GONE
                } else {
                    val errorMsg = response.error ?: "Неизвестная ошибка"
                    binding.tvEmpty.text = "Ошибка: $errorMsg"
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.tvEmpty.text = "Ошибка сети: ${e.message}"
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}