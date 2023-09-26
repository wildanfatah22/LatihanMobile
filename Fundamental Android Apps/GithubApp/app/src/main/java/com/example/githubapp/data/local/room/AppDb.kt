package com.example.githubapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubapp.data.local.entity.FavoriteEntity


@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class AppDb: RoomDatabase() {
    abstract fun favoriteDao():FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDb {
            if (INSTANCE == null) {
                synchronized(AppDb::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDb::class.java,
                        "favorite_database"
                    ).build()
                }
            }
            return INSTANCE as AppDb
        }
    }
}