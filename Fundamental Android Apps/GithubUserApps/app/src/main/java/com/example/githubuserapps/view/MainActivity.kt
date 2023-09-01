package com.example.githubuserapps.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapps.R
import com.example.githubuserapps.data.adapter.UserAdapter
import com.example.githubuserapps.data.response.User
import com.example.githubuserapps.databinding.ActivityMainBinding
import com.example.githubuserapps.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

//    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.getSearchUsers().observe(this, Observer {
            if (it != null) {
                userAdapter.setList(it)
                showLoading(false)
            }
        })


    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


//    private fun observeUserList() {
//        mainViewModel.userList.observe(this, Observer { users ->
//            userAdapter.submitList(users)
//        })
//
//        mainViewModel.error.observe(this, Observer { error ->
//            // Handle error here if needed
//        })
//    }
}