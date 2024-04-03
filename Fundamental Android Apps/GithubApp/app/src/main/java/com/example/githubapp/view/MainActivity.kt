package com.example.githubapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.data.adapter.UserAdapter
import com.example.githubapp.data.api.NetworkConnection
import com.example.githubapp.data.response.UserResponse
import com.example.githubapp.databinding.ActivityMainBinding
import com.example.githubapp.view.preferences.SettingPreferences
import com.example.githubapp.view.preferences.dataStore
import com.example.githubapp.viewmodel.MainViewModel
import com.example.githubapp.viewmodel.SettingViewModel
import com.example.githubapp.viewmodel.SettingViewModelFactory


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter: UserAdapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUI()
        supportActionBar?.hide()
    }

    private fun setUpUI() {
        setUpSearchView()
        setUpToolbar()
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
            override fun onItemClicked(user: UserResponse) {
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

    private fun navigateToUserDetail(user: UserResponse) {
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.KEY_USER, user)
        intent.putExtra(DetailUserActivity.KEY_USERNAME, user.login)
        intent.putExtra(DetailUserActivity.KEY_ID, user.id)
        intent.putExtra(DetailUserActivity.KEY_AVATAR, user.avatarUrl)
        startActivity(intent)
    }

    private fun setUpToolbar() {
        binding.materialToolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_language -> {
                val changeLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(changeLanguage)
                return true
            }
            R.id.action_favorite -> {
                val favorite = Intent(this, FavoriteActivity::class.java)
                startActivity(favorite)
                return true
            }
            R.id.action_setting -> {
                val setting = Intent(this, SettingActivity::class.java)
                startActivity(setting)
                return true
            }
            else -> false
        }
    }
}
