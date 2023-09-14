package com.example.githubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class FavoriteEntity(
    @ColumnInfo(name = "avatar_url")
    val avatar_url: String,
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String,
) : Parcelable

