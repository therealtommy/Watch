package com.example.watch.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watch.BuildConfig
import com.example.watch.domain.repository.MovieRepository
import com.example.watch.db.MovieDatabase
import com.example.watch.network.RetrofitClient

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val dao = MovieDatabase.getInstance(context).movieDao()
            val repository = MovieRepository(
                movieDao = dao,
                omdbApi = RetrofitClient.api,
                apiKey = "174065d5"
            )
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}