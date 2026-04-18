package com.example.watch.ui.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.watch.di.RepositoryProvider
import com.example.watch.domain.usecase.AddMovieUseCase

class AddViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = RepositoryProvider.provideRepository(context)
        val addMovieUseCase = AddMovieUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return AddViewModel(addMovieUseCase) as T
    }
}