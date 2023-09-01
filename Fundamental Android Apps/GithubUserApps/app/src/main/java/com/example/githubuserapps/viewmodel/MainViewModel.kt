package com.example.githubuserapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapps.data.response.User
import com.example.githubuserapps.data.response.UserResponse
import com.example.githubuserapps.data.response.UserSearchResponse
import com.example.githubuserapps.data.response.UsersItem
import com.example.githubuserapps.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class MainViewModel : ViewModel() {
//
//    private val apiService = ApiConfig.retrofit
//
//    private val _userList = MutableLiveData<List<UsersItem>>()
//    val userList: LiveData<List<UsersItem>> = _userList
//
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
//
//    private val _error = MutableLiveData<String>()
//    val error: LiveData<String> = _error
//
//    fun listUsers(username: String) {
//        _loading.postValue(true)
//
//        apiService.getSearch(username).enqueue(object : Callback<UserSearchResponse> {
//            override fun onResponse(
//                call: Call<UserSearchResponse>,
//                response: Response<UserSearchResponse>
//            ) {
//                _loading.postValue(false)
//
//                if (response.isSuccessful) {
//                    _userList.postValue(response.body()?.items)
//                } else {
//                    _error.postValue("Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
//                _loading.postValue(false)
//                _error.postValue("Error: ${t.message}")
//            }
//        })
//    }
//}



class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUsers(query: String) {
        ApiConfig.apiInstance
            .getSearch(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                }

            })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}