package com.example.githubapp.view

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.adapter.SectionPagerAdapter
import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.remote.api.NetworkConnection
import com.example.githubapp.data.remote.response.UserResponse
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
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        binding.detailDataLayout.visibility = View.VISIBLE
        setContentView(binding.root)
        supportActionBar?.hide()
        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        if (user != null) {
            user.login?.let {
                checkInternetConnection(it)

            }
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }


    private fun checkInternetConnection(username: String) {
        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                val favorite = FavoriteEntity()
                favorite.login = username
                favorite.id = intent.getIntExtra(KEY_ID, 0)
                favorite.avatar_url = user?.avatarUrl
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
                DetailViewModel.getFavoriteById(favorite.id!!)
                    .observe(this@DetailUserActivity) { listFav ->
                        isFavorite = listFav.isNotEmpty()
                        binding.btnFavorite.iconTint = if (listFav.isEmpty()) {
                            ColorStateList.valueOf(Color.rgb(255, 255, 255))
                        } else {
                            ColorStateList.valueOf(Color.rgb(247, 106, 123))
                        }

                    }

                binding.btnFavorite.apply {
                    setOnClickListener {
                        if (isFavorite) {
                            DetailViewModel.delete(favorite)
                            Toast.makeText(
                                this@DetailUserActivity,
                                "${favorite.login} telah dihapus dari data User Favorite ",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            DetailViewModel.insert(favorite)
                            Toast.makeText(
                                this@DetailUserActivity,
                                "${favorite.login} telah ditambahkan ke data User Favorite",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                binding.detailDataLayout.visibility = View.GONE
            }
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
                if (userResponse.bio != null) {
                    tvDetailBio.visibility = View.VISIBLE
                    tvDetailBio.text = userResponse.bio
                } else {
                    tvDetailBio.visibility = View.GONE
                }
                if (userResponse.company != null) {
                    tvDetailCompany.visibility = View.VISIBLE
                    tvDetailCompany.text = userResponse.company
                } else {
                    tvDetailCompany.visibility = View.GONE
                    ivIconCompany.visibility = View.GONE
                }
                if (userResponse.location != null) {
                    tvDetailLocation.visibility = View.VISIBLE
                    tvDetailLocation.text = userResponse.location
                } else {
                    tvDetailLocation.visibility = View.GONE
                    ivIconLocation.visibility = View.GONE
                }
                if (userResponse.blog != null) {
                    tvDetailBlog.visibility = View.VISIBLE
                    tvDetailBlog.text = userResponse.blog
                } else {
                    tvDetailBlog.visibility = View.GONE
                    ivIconBlog.visibility = View.GONE
                }
                if (userResponse.followers != null) {
                    tvDetailFollower.visibility = View.VISIBLE
                    tvDetailFollower.text = userResponse.followers
                } else {
                    tvDetailFollower.text = "0"
                }
                if (userResponse.following != null) {
                    tvDetailFollowing.visibility = View.VISIBLE
                    tvDetailFollowing.text = userResponse.following
                } else {
                    tvDetailFollowing.text = "0"
                }
                if (userResponse.public_repo != null) {
                    tvDetailRepository.visibility = View.VISIBLE
                    tvDetailRepository.text = userResponse.public_repo
                } else {
                    tvDetailRepository.text = "0"
                }
            }
        } else {
            Log.i(TAG, "setData is error")
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_USER = "user"
        private const val TAG = "DetailActivity"

        const val KEY_ID = "extra_id"
        const val KEY_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}