package com.example.finalgithubapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.finalgithubapp.R
import com.example.finalgithubapp.databinding.ActivityDetailUserBinding
import com.example.finalgithubapp.view.adapter.SectionPagerAdapter
import com.example.finalgithubapp.viewmodel.DetailUserActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.github_icon)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val username = intent.getStringExtra(KEY_USERNAME)
        val id = intent.getIntExtra(KEY_ID, 0)
        val avatarUrl = intent.getStringExtra(KEY_AVATAR)
        val bundle = Bundle()

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        viewModel = ViewModelProvider(this).get(
            DetailUserActivityViewModel::class.java
        )

        username?.let { showLoading(true)
            viewModel.setUserDetail(it) }
        viewModel.getUserDetail().observe(this, Observer {
            if (it != null) {
                binding.apply {
                    tvDetailUsername.text = it.login
                    tvDetailFollower.text = it.followers.toString()
                    tvDetailFollowing.text = it.following.toString()
                    tvDetailRepository.text = it.public_repo.toString()
                    tvDetailName.text = it.name
                    tvDetailLocation.text = it.location
                    tvDetailCompany.text = it.company
                    tvDetailBlog.text = it.blog
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(profileImage)
                    showLoading(false)
                }
            }
        })

        var isChacked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnFavorite.isChecked = true
                        isChacked = true
                    } else {
                        binding.btnFavorite.isChecked = false
                        isChacked = false
                    }
                }
            }
        }

        binding.btnFavorite.setOnClickListener {
            isChacked = !isChacked
            if (isChacked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addToFavorite(username, id, avatarUrl)
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.btnFavorite.isChecked = isChacked
        }

    }

    companion object {
        const val KEY_USER = "user"
        private const val TAG = "DetailActivity"
        const val KEY_USERNAME = "username"
        const val KEY_ID = "extra_id"
        const val KEY_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}