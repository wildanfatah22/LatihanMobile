package com.example.githubapp.data.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.response.UserResponse
import com.example.githubapp.databinding.ItemUserBinding


class UserAdapter: RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private var listUserResponse = ArrayList<UserResponse>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(items: ArrayList<UserResponse>) {
        listUserResponse.clear()
        listUserResponse.addAll(items)
        notifyDataSetChanged()
    }



    class MyViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userResponse: UserResponse) {
            binding.tvUsername.text = userResponse.login
            Glide.with(binding.root)
                .load(userResponse.avatarUrl)
                .circleCrop()
                .into(binding.photoGithub)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listUserResponse.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUserResponse[position])
        holder.itemView.setOnClickListener { onItemClickCallback?.onItemClicked(listUserResponse[position]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserResponse)
    }

}