package com.example.watch.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watch.di.RepositoryProvider
import com.example.watch.domain.usecase.DeleteMoviesUseCase
import com.example.watch.domain.usecase.GetWatchlistUseCase

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = RepositoryProvider.provideRepository(context)
        val getWatchlistUseCase = GetWatchlistUseCase(repository)
        val deleteMoviesUseCase = DeleteMoviesUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(getWatchlistUseCase, deleteMoviesUseCase) as T
    }
}