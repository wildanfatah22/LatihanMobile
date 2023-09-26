package com.example.finalgithubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "User"
)
@Parcelize
data class FavoriteEntity(
    @ColumnInfo(name = "login")
    var login: String,

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String,
) : Parcelable