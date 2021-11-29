package com.example.githubuserrev6

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserrev6.databinding.ListRepositoryUserGithubBinding

class UserRepositoryAdapter :
    RecyclerView.Adapter<UserRepositoryAdapter.ListRepositoryViewHolder>() {
    private var dataUserGithub = arrayListOf<UserGithub>()

    fun listRepositoryUserGithub(arrayUserGithub: ArrayList<UserGithub>) {
        val diffCallback = ListRepositoryDiff(this.dataUserGithub, arrayUserGithub)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        this.dataUserGithub = arrayUserGithub
    }

    inner class ListRepositoryViewHolder(private var listRepositoryBinding: ListRepositoryUserGithubBinding) :
        RecyclerView.ViewHolder(listRepositoryBinding.root) {
        fun binding(userGithub: UserGithub) {
            listRepositoryBinding.repositoryName.text = userGithub.repositoryName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRepositoryViewHolder {
        val listRepositoryView = ListRepositoryUserGithubBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ListRepositoryViewHolder(listRepositoryView)
    }

    override fun onBindViewHolder(holder: ListRepositoryViewHolder, position: Int) =
        holder.binding(dataUserGithub[position])

    override fun getItemCount(): Int = dataUserGithub.size
}