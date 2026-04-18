package com.example.watch.di

import android.content.Context
import com.example.watch.data.repository.MovieRepositoryImpl
import com.example.watch.data.source.LocalDataSourceImpl
import com.example.watch.data.source.RemoteDataSourceImpl
import com.example.watch.db.MovieDatabase
import com.example.watch.domain.repository.MovieRepository
import com.example.watch.network.RetrofitClient

object RepositoryProvider {
    fun provideRepository(context: Context): MovieRepository {
        val database = MovieDatabase.getInstance(context)
        val dao = database.movieDao()
        val localDataSource = LocalDataSourceImpl(dao)
        val remoteDataSource = RemoteDataSourceImpl(RetrofitClient.api, "174065d5")
        return MovieRepositoryImpl(localDataSource, remoteDataSource)
    }
}