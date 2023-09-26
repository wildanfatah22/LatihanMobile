package com.example.finalgithubapp.data.local

import androidx.lifecycle.LiveData
import com.example.finalgithubapp.data.local.entity.FavoriteEntity
import com.example.finalgithubapp.data.local.room.FavoriteDao

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    suspend fun addToFavorite(favoriteEntity: FavoriteEntity) {
        favoriteDao.addToFavorite(favoriteEntity)
    }

    fun getFavoriteUsers(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getFavoriteUser()
    }

    suspend fun checkUser(id: Int): Int {
        return favoriteDao.checkUser(id)
    }

    suspend fun removeFromFavorite(id: Int): Int {
        return favoriteDao.removeFromFavorite(id)
    }
}
