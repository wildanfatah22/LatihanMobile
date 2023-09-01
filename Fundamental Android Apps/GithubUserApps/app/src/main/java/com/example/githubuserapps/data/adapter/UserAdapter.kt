package com.example.githubuserapps.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapps.data.response.User
import com.example.githubuserapps.data.response.UsersItem
import com.example.githubuserapps.databinding.ItemRowUserBinding

//class UserAdapter : ListAdapter<UsersItem, UserAdapter.UserViewHolder>(UserDiffCallback()) {
//
//    class UserViewHolder(private val binding: ItemRowUserBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(user: UsersItem) {
//            binding.tvUsername.text = user.login
//            Glide.with(binding.root.context)
//                .load(user.avatarUrl)
//                .into(binding.photoGithub)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return UserViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user = getItem(position)
//        holder.bind(user)
//    }
//
//    companion object {
//        class UserDiffCallback : DiffUtil.ItemCallback<UsersItem>() {
//            override fun areItemsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }


    inner class UserViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .centerCrop()
                    .into(photoGithub)
                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}