package com.example.githubuserapps.data.response

import com.google.gson.annotations.SerializedName

data class User(
    val name: String?,
    val login: String?,
    @field:SerializedName("avatar_url")
    val avatar_url: String?,
    val bio: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    @field:SerializedName("public_repos")
    val publicRepo: String?,
    val followers: String?,
    val following: String?,
    @field:SerializedName("followers_url")
    val followersUrl: String?,
    @field:SerializedName("following_url")
    val followingUrl: String?
)
