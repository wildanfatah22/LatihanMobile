package com.example.githubapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.adapter.UserAdapter
import com.example.githubapp.data.adapter.UserFollowAdapter
import com.example.githubapp.data.response.UserResponse
import com.example.githubapp.databinding.FragmentFollowBinding
import com.example.githubapp.viewmodel.FollowViewModel
import com.example.githubapp.viewmodel.FollowViewModelFactory


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

    private fun setUserData(userResponse: ArrayList<UserResponse>) {
        if (userResponse.isNotEmpty()) {
            adapter.addDataToList(userResponse)
            with(binding) {
                val layoutManager = LinearLayoutManager(view?.context)
                rvFollow.layoutManager = layoutManager
                rvFollow.adapter = adapter
                adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: UserResponse) {
                        val intent = Intent(context, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.KEY_USER, user)
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