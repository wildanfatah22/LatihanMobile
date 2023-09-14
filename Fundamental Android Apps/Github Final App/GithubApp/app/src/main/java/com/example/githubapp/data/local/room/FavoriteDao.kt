package com.example.githubapp.data.local.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.githubapp.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    fun addToFavorite(favoriteUser: FavoriteEntity)

    @Query ("SELECT * FROM favorite")
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query ("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    fun checkUser(id:Int):Int

//    @Query("DELETE FROM favorite WHERE favorite.id = :id")
//    fun removeFromFavorite(id: Int):Int

    @Delete
    fun delete(fav: FavoriteEntity)

    @Query ("SELECT * FROM favorite")
    fun findAll(): Cursor

    @Query("SELECT  * from favorite WHERE id = :id")
    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteEntity>>

}