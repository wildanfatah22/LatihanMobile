package com.example.finalgithubapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalgithubapp.R
import com.example.finalgithubapp.data.api.NetworkConnection
import com.example.finalgithubapp.data.responses.User
import com.example.finalgithubapp.databinding.ActivityMainBinding
import com.example.finalgithubapp.view.adapter.UserAdapter
import com.example.finalgithubapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter: UserAdapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
        supportActionBar?.hide()
    }

    private fun setUpUI() {
        setUpSearchView()
        setUpRecyclerView()
        observeData()
        checkInternetConnection()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchUser(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                navigateToUserDetail(user)
            }
        })
    }


    private fun observeData() {
        mainViewModel.isLoading.observe(this) { showProgressBar(it) }
        mainViewModel.user.observe(this) { userResponse ->
            userResponse?.let {
                adapter.addDataToList(it)
            }
        }
        mainViewModel.searchUser.observe(this) { searchUserResponse ->
            searchUserResponse?.let {
                adapter.addDataToList(it)
                binding.rvUser.visibility = View.VISIBLE
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun checkInternetConnection() {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (!isConnected) {
                Toast.makeText(this@MainActivity, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun searchUser(query: String) {
        mainViewModel.getUserBySearch(query)
    }

    private fun navigateToUserDetail(user: User) {
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.KEY_USERNAME, user.login)
        intent.putExtra(DetailUserActivity.KEY_ID, user.id)
        intent.putExtra(DetailUserActivity.KEY_AVATAR, user.avatarUrl)
        startActivity(intent)
    }
}