package com.example.aplikasigithubuser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.service.response.ItemsItem
import com.example.aplikasigithubuser.R

class UserAdapter(private val listUser: List<ItemsItem>, private val onClick: (ItemsItem, View) -> Unit
    ) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val username: String? = listUser[position].login
        val avatar: String? = listUser[position].avatarUrl

        viewHolder.tvUser.text = username
        Glide.with(viewHolder.itemView.context)
            .load(avatar).circleCrop()
            .into(viewHolder.ivUser)

        viewHolder.itemView.setOnClickListener {view ->
            onClick(listUser[position], view)
        }
    }

    override fun getItemCount() = listUser.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivUser: ImageView = view.findViewById(R.id.iv_user)
        val tvUser: TextView = view.findViewById(R.id.tv_user)
    }
}



