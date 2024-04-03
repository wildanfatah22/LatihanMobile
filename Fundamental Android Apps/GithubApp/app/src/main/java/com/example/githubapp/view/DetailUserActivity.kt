package com.example.githubapp.view

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.adapter.SectionPagerAdapter
import com.example.githubapp.data.api.NetworkConnection
import com.example.githubapp.data.response.UserResponse
import com.example.githubapp.databinding.ActivityDetailUserBinding
import com.example.githubapp.viewmodel.DetailViewModel
import com.example.githubapp.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val DetailViewModel by viewModels<DetailViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        binding.detailDataLayout.visibility = View.VISIBLE
        setContentView(binding.root)
        supportActionBar?.hide()
        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        user?.login?.let {
            checkInternetConnection(it, application)

        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun checkInternetConnection(username: String, application: Application) {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                val detailViewModel: DetailViewModel by viewModels {
                    DetailViewModelFactory(username, application)
                }

                detailViewModel.isLoading.observe(this) {
                    showProgressBar(it)
                }

                detailViewModel.detailUser.observe(this@DetailUserActivity) { userResponse ->
                    if (userResponse != null) {
                        showProgressBar(false)
                        setData(userResponse)
                        setTabLayoutAdapter(userResponse)
                    }
                }


                val name = intent.getStringExtra(KEY_USERNAME)
                val id = intent.getIntExtra(KEY_ID, 0)
                val avatarUrl = intent.getStringExtra(KEY_AVATAR)

                val btnFavorite = binding.btnFavorite


                var isFavorite = false
                CoroutineScope(Dispatchers.IO).launch {
                    val count = DetailViewModel.checkUser(id)
                    withContext(Dispatchers.Main) {
                        if (count != null) {
                            isFavorite = count > 0
                            updateFavoriteButton(isFavorite)
                        }
                    }
                }

                btnFavorite.setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        if (name != null && avatarUrl != null) {
                            addToFavorites(name, id, avatarUrl)
                        }
                    } else {
                        removeFromFavorites(id)
                    }
                    updateFavoriteButton(isFavorite)
                }


            } else {
                binding.detailDataLayout.visibility = View.GONE
            }
        }
    }


    private fun addToFavorites(name: String, id: Int, avatarUrl: String) {
        DetailViewModel.addToFavorite(name, id, avatarUrl)
        showToast(getString(R.string.pengguna_ditambahkan_ke_favorit))
    }

    private fun removeFromFavorites(id: Int) {
        DetailViewModel.deleteInt(id)
        showToast(getString(R.string.pengguna_dihapus_dari_favorit))
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        val btnFavorite = binding.btnFavorite
        btnFavorite.isChecked = isFavorite
        if (isFavorite) {
            btnFavorite.text = getString(R.string.hapus_dari_favorit)
            btnFavorite.iconTint = ColorStateList.valueOf(Color.rgb(247, 106, 123))
            btnFavorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white)
        } else {
            btnFavorite.text = getString(R.string.tambahkan_ke_favorit)
            btnFavorite.iconTint = ColorStateList.valueOf(Color.GRAY)
            btnFavorite.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border)
        }
    }

    private fun setTabLayoutAdapter(user: UserResponse) {
        val sectionPagerAdapter = SectionPagerAdapter(this@DetailUserActivity)
        sectionPagerAdapter.model = user
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setData(userResponse: UserResponse?) {
        if (userResponse != null) {
            with(binding) {
                detailDataLayout.visibility = View.VISIBLE
                profileImage.visibility = View.VISIBLE
                Glide.with(root)
                    .load(userResponse.avatarUrl)
                    .circleCrop()
                    .into(binding.profileImage)
                tvDetailName.visibility = View.VISIBLE
                tvDetailUsername.visibility = View.VISIBLE
                tvDetailName.text = userResponse.name
                tvDetailUsername.text = userResponse.login
                if (!userResponse.bio.isNullOrEmpty()) {
                    tvDetailBio.visibility = View.VISIBLE
                    tvDetailBio.text = userResponse.bio
                } else {
                    tvDetailBio.visibility = View.GONE
                }
                if (!userResponse.company.isNullOrEmpty()) {
                    tvDetailCompany.visibility = View.VISIBLE
                    tvDetailCompany.text = userResponse.company
                } else {
                    tvDetailCompany.visibility = View.GONE
                    ivIconCompany.visibility = View.GONE
                }
                if (!userResponse.location.isNullOrEmpty()) {
                    tvDetailLocation.visibility = View.VISIBLE
                    tvDetailLocation.text = userResponse.location
                } else {
                    tvDetailLocation.visibility = View.GONE
                    ivIconLocation.visibility = View.GONE
                }
                if (!userResponse.blog.isNullOrEmpty()) {
                    tvDetailBlog.visibility = View.VISIBLE
                    tvDetailBlog.text = userResponse.blog
                } else {
                    tvDetailBlog.visibility = View.GONE
                    ivIconBlog.visibility = View.GONE
                }
                tvDetailFollower.text = userResponse.followers ?: "0"
                tvDetailFollowing.text = userResponse.following ?: "0"
                tvDetailRepository.text = userResponse.public_repo ?: "0"
            }
        } else {
            Log.i(TAG, "setData is error")
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val KEY_USER = "user"
        private const val TAG = "DetailActivity"
        const val KEY_USERNAME = "username"
        const val KEY_ID = "extra id"
        const val KEY_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}