package com.example.githubuserapp.data.retrofit

import com.example.githubuserapp.data.response.DetailResponse
import com.example.githubuserapp.data.response.ListFollowersResponse
import com.example.githubuserapp.data.response.ListFollowingsResponse
import com.example.githubuserapp.data.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token <Your Personal Access Token>")
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<UserSearchResponse>


    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<ListFollowersResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<ListFollowingsResponse>>
}