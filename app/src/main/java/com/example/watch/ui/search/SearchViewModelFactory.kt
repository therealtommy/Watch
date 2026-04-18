package com.example.watch.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watch.di.RepositoryProvider
import com.example.watch.domain.usecase.SearchMoviesUseCase

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = RepositoryProvider.provideRepository(context)
        val searchMoviesUseCase = SearchMoviesUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(searchMoviesUseCase) as T
    }
}