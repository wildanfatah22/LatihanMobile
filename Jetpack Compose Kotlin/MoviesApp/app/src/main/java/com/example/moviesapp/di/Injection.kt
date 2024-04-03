package com.example.moviesapp.di

import com.example.moviesapp.data.MovieRepository


object Injection {
    fun provideRepository(): MovieRepository {
        return MovieRepository.getInstance()
    }
}