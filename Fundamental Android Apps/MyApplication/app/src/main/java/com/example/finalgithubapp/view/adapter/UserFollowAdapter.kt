package com.example.finalgithubapp.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalgithubapp.data.responses.User
import com.example.finalgithubapp.databinding.ItemUserBinding

class UserFollowAdapter:
    RecyclerView.Adapter<UserFollowAdapter.MyViewHolder>() {
    private var listUserResponse = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(items: ArrayList<User>) {
        listUserResponse.clear()
        listUserResponse.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUserResponse[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUserResponse[position]) }
    }

    override fun getItemCount() = listUserResponse.size

    class MyViewHolder(private var binding:  ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userResponse: User) {
            binding.tvUsername.text = userResponse.login
            Glide.with(binding.root)
                .load(userResponse.avatarUrl)
                .circleCrop()
                .into(binding.photoGithub)
        }
    }

    private lateinit var onItemClickCallback: UserAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: UserAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}