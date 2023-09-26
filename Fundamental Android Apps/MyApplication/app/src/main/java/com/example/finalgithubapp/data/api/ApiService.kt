package com.example.finalgithubapp.data.api

import com.example.finalgithubapp.BuildConfig
import com.example.finalgithubapp.data.responses.SearchResponse
import com.example.finalgithubapp.data.responses.User
import com.example.finalgithubapp.data.responses.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val API_KEY = BuildConfig.API_KEY
    }

    @GET("users")
    @Headers("Authorization: token $API_KEY", "UserResponse-Agent: request")
    suspend fun getListUsersAsync(): ArrayList<User>

    @GET("users/{username}")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    fun getDetailUserAsync(@Path("username") username: String): Call<UserResponse>

    @GET("search/users")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    fun getUserBySearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    suspend fun getListFollowers(@Path("username") username: String): ArrayList<User>

    @GET("users/{username}/following")
    @Headers("Authorization: $API_KEY", "UserResponse-Agent: request")
    suspend fun getListFollowing(@Path("username") username: String): ArrayList<User>
}