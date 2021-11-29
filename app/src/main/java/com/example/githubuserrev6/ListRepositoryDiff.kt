package com.example.githubuserrev6

import androidx.recyclerview.widget.DiffUtil

class ListRepositoryDiff(
    private val oldRepository: ArrayList<UserGithub>,
    private val newRepository: ArrayList<UserGithub>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldRepository.size

    override fun getNewListSize(): Int = newRepository.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldRepository[oldItemPosition].repositoryName == newRepository[newItemPosition].repositoryName

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPosition = oldRepository[oldItemPosition]
        val newPosition = newRepository[oldItemPosition]

        return oldPosition.repositoryName == newPosition.repositoryName
    }
}