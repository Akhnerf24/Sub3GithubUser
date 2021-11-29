package com.example.githubuserrev6

import androidx.recyclerview.widget.DiffUtil

class ListUsernameDiff(
    private val oldArrayData: ArrayList<UserGithub>,
    private val newArrayData: ArrayList<UserGithub>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldArrayData.size

    override fun getNewListSize(): Int = newArrayData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldArrayData[oldItemPosition].username == newArrayData[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPosition = oldArrayData[oldItemPosition]
        val newPosition = newArrayData[newItemPosition]

        return oldPosition.username == newPosition.username
    }
}