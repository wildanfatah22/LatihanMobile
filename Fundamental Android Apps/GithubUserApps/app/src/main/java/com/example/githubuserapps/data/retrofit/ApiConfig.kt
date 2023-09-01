package com.example.githubuserapps.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//class ApiConfig {
//    companion object{
//        fun getApiService(): ApiService {
//            val loggingInterceptor =
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build()
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//            return retrofit.create(ApiService::class.java)
//        }
//    }
//}

object ApiConfig {
    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiInstance: ApiService = retrofit.create(ApiService::class.java)
}