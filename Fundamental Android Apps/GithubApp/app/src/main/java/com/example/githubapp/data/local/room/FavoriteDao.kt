package com.example.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubapp.data.local.entity.FavoriteEntity


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(favoriteUser: FavoriteEntity)

    @Query ("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query ("SELECT count(*) FROM user WHERE user.id = :id")
    suspend fun checkUser(id:Int):Int

    @Query("DELETE FROM user WHERE user.id = :id")
    suspend fun delete(id: Int):Int

    @Delete
    suspend fun removeFromFavorite(favoriteUser: FavoriteEntity)

}