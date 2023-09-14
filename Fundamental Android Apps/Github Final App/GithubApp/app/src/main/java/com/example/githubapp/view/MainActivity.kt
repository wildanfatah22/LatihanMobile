package com.example.githubapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.data.adapter.UserAdapter
import com.example.githubapp.data.remote.api.NetworkConnection
import com.example.githubapp.data.remote.response.UserResponse
import com.example.githubapp.databinding.ActivityMainBinding
import com.example.githubapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    private val adapter: UserAdapter by lazy {
        UserAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpSearchView()
        setUpToolbar()
        observeAnimationAndProgressBar()
        checkInternetConnection()
        supportActionBar?.hide()
    }

    private fun setUpSearchView() {
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.getUserBySearch(query)
                    mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                        if (searchUserResponse != null) {
                            adapter.addDataToList(searchUserResponse)
                            setUserData()
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }


    private fun hideUserList() {
        binding.rvUser.layoutManager = null
        binding.rvUser.adapter = null
    }

    private fun setUserData() {
        val layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                hideUserList()
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.KEY_USER, user)
                startActivity(intent)
            }
        })
    }

    private fun showProgressBar(state: Boolean) {
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    private fun observeAnimationAndProgressBar() {
        mainViewModel.isLoading.observe(this) {
            showProgressBar(it)
        }
    }

    private fun checkInternetConnection() {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                mainViewModel.user.observe(this) { userResponse ->
                    if (userResponse != null) {
                        adapter.addDataToList(userResponse)
                        setUserData()
                    }
                }
                mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                    if (searchUserResponse != null) {
                        adapter.addDataToList(searchUserResponse)
                        binding.rvUser.visibility = View.VISIBLE
                    }
                }
            } else {
                mainViewModel.user.observe(this) { userResponse ->
                    if (userResponse != null) {
                        adapter.addDataToList(userResponse)
                        setUserData()
                    }
                }
                mainViewModel.searchUser.observe(this@MainActivity) { searchUserResponse ->
                    if (searchUserResponse != null) {
                        adapter.addDataToList(searchUserResponse)
                        binding.rvUser.visibility = View.VISIBLE
                    }
                }
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                // Tindakan yang harus diambil saat item "Favorite" ditekan
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                return true // Menandakan bahwa item menu ini telah ditangani
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        binding.materialToolbar.setOnMenuItemClickListener(this)
    }

}