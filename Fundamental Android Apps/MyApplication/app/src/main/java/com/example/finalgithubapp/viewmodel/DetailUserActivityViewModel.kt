package com.example.finalgithubapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalgithubapp.data.api.ApiConfig
import com.example.finalgithubapp.data.local.entity.FavoriteEntity
import com.example.finalgithubapp.data.local.room.AppDb
import com.example.finalgithubapp.data.local.room.FavoriteDao
import com.example.finalgithubapp.data.responses.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivityViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<UserResponse>()

    private var userDao: FavoriteDao?
    private var userDb: AppDb? = AppDb.getDatabase(application)

    init {
        userDao = userDb?.favoriteDao()
    }


    fun setUserDetail(username: String) {
        ApiConfig.getApiService()
            .getDetailUserAsync(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }
            })
    }

    fun getUserDetail(): LiveData<UserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteEntity(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}