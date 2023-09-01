package com.example.githubuserapps.data.retrofit

import com.example.githubuserapps.data.response.UserResponse
import com.example.githubuserapps.data.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    @Headers("Authorization: token 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")
    fun getSearch(
        @Query("q") query: String
    ): Call<UserResponse>
}