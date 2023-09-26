package com.example.githubapp.data.api

import com.example.githubapp.data.response.SearchResponse
import com.example.githubapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val API_KEY = com.example.githubapp.BuildConfig.API_KEY
    }

    @GET("users")
    @Headers("Authorization: token $API_KEY", "UserResponse-Agent: request")
    suspend fun getListUsersAsync(): ArrayList<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    suspend fun getDetailUserAsync(@Path("username") username: String): UserResponse

    @GET("search/users")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    fun getUserBySearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    suspend fun getListFollowers(@Path("username") username: String): ArrayList<UserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    suspend fun getListFollowing(@Path("username") username: String): ArrayList<UserResponse>
}