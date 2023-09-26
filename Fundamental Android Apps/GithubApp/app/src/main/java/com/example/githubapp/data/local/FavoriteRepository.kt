package com.example.githubapp.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.local.room.AppDb
import com.example.githubapp.data.local.room.FavoriteDao

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    init {
        val db = AppDb.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }


    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getFavoriteUser()

    suspend fun delete(id: Int) = mFavoriteDao.delete(id)

    suspend fun addToFavorite(user: FavoriteEntity) = mFavoriteDao.addToFavorite(user)


    suspend fun checkUser(id: Int): Int = mFavoriteDao.checkUser(id)
}