package com.example.githubapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.local.FavoriteRepository
import com.example.githubapp.data.local.entity.FavoriteEntity

class FavoriteViewModel(application: Application): ViewModel() {

    private val mFavRepository : FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites() : LiveData<List<FavoriteEntity>> = mFavRepository.getAllFavorites()
}