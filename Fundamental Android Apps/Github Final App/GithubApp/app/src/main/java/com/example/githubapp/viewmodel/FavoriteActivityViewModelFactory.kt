package com.example.githubapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteActivityViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: FavoriteActivityViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): FavoriteActivityViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteActivityViewModelFactory::class.java) {
                    INSTANCE = FavoriteActivityViewModelFactory(application)
                }
            }
            return INSTANCE as FavoriteActivityViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteActivityViewModel::class.java)) {
            return FavoriteActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name} ")
    }
}