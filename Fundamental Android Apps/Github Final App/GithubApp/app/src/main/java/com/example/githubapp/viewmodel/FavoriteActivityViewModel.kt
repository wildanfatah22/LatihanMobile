package com.example.githubapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.repository.FavoriteRepository

class FavoriteActivityViewModel(application: Application): ViewModel() {
    private val mFavRepository : FavoriteRepository = FavoriteRepository(application)
    fun getFavoriteUser() : LiveData<List<FavoriteEntity>> = mFavRepository.getAllFavorites()
}