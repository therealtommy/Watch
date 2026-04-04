package com.example.watch.ui.search

import android.os.Bundle
import android.util.Log
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

        // Получаем аргументы из Bundle вручную
        val query = arguments?.getString("query") ?: ""
        val year = arguments?.getString("year")
        performSearch(query, year)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            // Передаём выбранный фильм обратно на AddFragment через Bundle
            val bundle = Bundle().apply {
                putParcelable("selectedMovie", movie)
            }
            findNavController().navigate(R.id.action_search_to_add, bundle)
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun performSearch(query: String, year: String?) {
        lifecycleScope.launch {
            //Log.d("API_KEY_TEST", "API_KEY = ${BuildConfig.API_KEY}")
            try {
                val response = RetrofitClient.api.searchMovies("174065d5", query, year)
                if (response.response == "True") {
                    adapter.submitList(response.search ?: emptyList())
                    binding.tvEmpty.visibility = View.GONE
                } else {
                    // Логируем и показываем конкретную ошибку от OMDb
                    val errorMsg = response.error ?: "Неизвестная ошибка API"
                    Log.e("SearchFragment", "Ошибка OMDb API: $errorMsg")
                    binding.tvEmpty.text = "Ошибка OMDb: $errorMsg"
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                // Логируем техническую ошибку (нет сети, таймаут, и т.д.)
                Log.e("SearchFragment", "Техническая ошибка при запросе", e)
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