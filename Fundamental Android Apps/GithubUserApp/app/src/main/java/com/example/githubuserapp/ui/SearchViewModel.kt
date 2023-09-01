package com.example.githubuserapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.response.UserItem
import com.example.githubuserapp.data.response.UserSearchResponse
import com.example.githubuserapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _userList = MutableLiveData<List<UserItem>>()
    val userList: LiveData<List<UserItem>> = _userList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun searchUsers(username: String) {
        apiService.searchUsers(username).enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    _userList.postValue(response.body()?.items)
                } else {
                    _error.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _error.postValue("Error: ${t.message}")
            }
        })
    }


}