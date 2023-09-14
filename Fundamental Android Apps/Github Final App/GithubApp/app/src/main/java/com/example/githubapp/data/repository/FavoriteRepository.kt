package com.example.githubapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.local.room.FavoriteDao
import com.example.githubapp.data.local.room.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }
    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getFavoriteUser()

    fun insert(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.addToFavorite(favorite) }
    }

    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteEntity>> =
        mFavoriteDao.getUserFavoriteById(id)

//    fun delete(id: Int) {
//        executorService.execute { mFavoriteDao.removeFromFavorite(id) }
//    }
    fun delete(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }
}