package com.example.githubapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.adapter.FavoriteAdapter
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.local.entity.toUserResponse
import com.example.githubapp.databinding.ActivityFavoriteBinding
import com.example.githubapp.viewmodel.FavoritViewModelFactory
import com.example.githubapp.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        setUpList()
        setUserFavorite()
        supportActionBar?.hide()

        binding?.backButton?.setOnClickListener {
            finish()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoritViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setUpList() {
        with(binding) {
            val layoutManager = LinearLayoutManager(this@FavoriteActivity)
            this?.rvFavorite?.layoutManager = layoutManager
            val itemDecoration =
                DividerItemDecoration(this@FavoriteActivity, layoutManager.orientation)
            this?.rvFavorite?.addItemDecoration(itemDecoration)
            this?.rvFavorite?.adapter = adapter
            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(favEntity: FavoriteEntity) {
                    val user = favEntity.toUserResponse() // Mengonversi FavoriteEntity ke UserResponse
                    val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.KEY_USER, user)
                    intent.putExtra(DetailUserActivity.KEY_USERNAME, user.login)
                    intent.putExtra(DetailUserActivity.KEY_ID, user.id)
                    intent.putExtra(DetailUserActivity.KEY_AVATAR, user.avatarUrl)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setUserFavorite() {
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this@FavoriteActivity) { favList ->
            if (favList != null) {
                adapter.setListFavorite(favList)
            }
            if (favList.isEmpty()) {
                showNoDataSaved(true)
            } else {
                showNoDataSaved(false)

            }
        }
    }
    private fun showNoDataSaved(isNoData: Boolean) {
        binding?.favNoData?.visibility = if (isNoData) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}