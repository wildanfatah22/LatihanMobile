package com.example.finalgithubapp.data.local.room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.finalgithubapp.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addToFavorite(favEntity: FavoriteEntity)

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query("SELECT count(*) FROM user WHERE user.id = :id")
    suspend fun checkUser(id:Int):Int

    @Query("DELETE FROM user WHERE user.id = :id")
    suspend fun removeFromFavorite(id: Int):Int


}