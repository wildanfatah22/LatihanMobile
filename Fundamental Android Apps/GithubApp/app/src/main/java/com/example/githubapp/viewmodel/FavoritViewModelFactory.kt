package com.example.githubapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FavoritViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: FavoritViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavoritViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoritViewModelFactory::class.java) {
                    INSTANCE = FavoritViewModelFactory(application)
                }
            }
            return INSTANCE as FavoritViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name} ")
    }

}