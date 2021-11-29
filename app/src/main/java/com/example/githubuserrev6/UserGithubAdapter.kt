package com.example.githubuserrev6

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_DETAIL_USER
import com.example.githubuserrev6.databinding.ListUserGithubBinding

class UserGithubAdapter (private var activity: Activity)
    : RecyclerView.Adapter<UserGithubAdapter.ListUserViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var dataUserGithub = arrayListOf<UserGithub>()

    interface OnItemClickCallback {
        fun setItemClicked(data: UserGithub)
    }

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallBack
    }

    fun listUserGithub(arrayUserGithub: ArrayList<UserGithub>) {
        val diffCallback = ListUsernameDiff(this.dataUserGithub, arrayUserGithub)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.dataUserGithub = arrayUserGithub
    }

    inner class ListUserViewHolder(private val listUserBinding: ListUserGithubBinding) :
        RecyclerView.ViewHolder(listUserBinding.root) {
        fun listBinding(userGithub: UserGithub) {
            with(listUserBinding) {
                name.text = userGithub.username

                Glide.with(itemView.context)
                    //.load(Uri.parse(userGithub.avatar_url))
                    .load(userGithub.avatar_url)
                    .apply(RequestOptions())
                    .into(userPicture)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, UserDetailActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL_USER, userGithub.username)
                    activity.startActivity(intent)
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val listUsers =
            ListUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListUserViewHolder(listUsers)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.listBinding(dataUserGithub[position])
    }

    override fun getItemCount(): Int = dataUserGithub.size

}