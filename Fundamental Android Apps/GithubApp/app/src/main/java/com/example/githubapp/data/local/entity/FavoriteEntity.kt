package com.example.githubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubapp.data.response.UserResponse
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "User"
)
@Parcelize
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "login")
    var login: String,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String,
) : Parcelable

fun FavoriteEntity.toUserResponse(): UserResponse {
    return UserResponse(
        gistsUrl = "",
        reposUrl = "",
        followingUrl = "",
        starredUrl = "",
        login = this.login,
        followersUrl = "",
        type = "",
        url = "",
        subscriptionsUrl = "",
        score = 0,
        receivedEventsUrl = "",
        avatarUrl = this.avatar_url,
        eventsUrl = "",
        htmlUrl = "",
        siteAdmin = false,
        id = this.id,
        gravatarId = "",
        nodeId = "",
        organizationsUrl = "",
        name = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        bio = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        company = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        location = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        blog = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        followers = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        following = null,  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
        public_repo = null  // Isi dengan nilai yang sesuai jika tersedia di FavoriteEntity
    )
}