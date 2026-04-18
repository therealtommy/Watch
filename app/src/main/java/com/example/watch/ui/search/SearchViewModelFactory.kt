package com.example.watch.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watch.BuildConfig
import com.example.watch.data.MovieRepository
import com.example.watch.db.MovieDatabase
import com.example.watch.network.RetrofitClient

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val dao = MovieDatabase.getInstance(context).movieDao()
            val repository = MovieRepository(
                movieDao = dao,
                omdbApi = RetrofitClient.api,
                apiKey = "174065d5"
            )
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}