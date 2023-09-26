package com.example.finalgithubapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalgithubapp.R
import com.example.finalgithubapp.data.responses.User
import com.example.finalgithubapp.data.responses.UserResponse
import com.example.finalgithubapp.databinding.FragmentFollowBinding
import com.example.finalgithubapp.view.adapter.UserAdapter
import com.example.finalgithubapp.view.adapter.UserFollowAdapter
import com.example.finalgithubapp.viewmodel.FollowViewModel
import com.example.finalgithubapp.viewmodel.FollowViewModelFactory


class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val adapter: UserFollowAdapter by lazy {
        UserFollowAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val user = arguments?.getParcelable<UserResponse>(ARG_PARCEL)

        if (index == 1) {
            user?.login?.let {
                val mIndex = 1
                setViewModel(it, mIndex)
            }
        } else {
            user?.login?.let {
                val mIndex = 2
                setViewModel(it, mIndex)
            }
        }
    }

    private fun setViewModel(username: String, index: Int) {
        val followViewModel: FollowViewModel by viewModels {
            FollowViewModelFactory(username)
        }
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showProgressBar(it)
        }
        if (index == 1) {
            followViewModel.followers.observe(viewLifecycleOwner) { follResponse ->
                if (follResponse != null) {
                    setUserData(follResponse)
                }
            }
        } else {
            followViewModel.following.observe(viewLifecycleOwner) { follResponse ->
                if (follResponse != null) {
                    setUserData(follResponse)
                }
            }

        }
    }

    private fun setUserData(user: ArrayList<User>) {
        if (user.isNotEmpty()) {
            adapter.addDataToList(user)
            with(binding) {
                val layoutManager = LinearLayoutManager(view?.context)
                rvFollow.layoutManager = layoutManager
                rvFollow.adapter = adapter
                adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: User) {
                        val intent = Intent(context, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.KEY_USERNAME, user.login)
                        intent.putExtra(DetailUserActivity.KEY_ID, user.id)
                        intent.putExtra(DetailUserActivity.KEY_AVATAR, user.avatarUrl)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun showProgressBar(state: Boolean) {
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_PARCEL = "user_model"

        @JvmStatic
        fun newInstance(index: Int, userResponse: UserResponse?) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putParcelable(ARG_PARCEL, userResponse)
                }
            }
    }
}