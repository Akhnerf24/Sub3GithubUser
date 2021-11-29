package com.example.githubuserrev6

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_AVATAR_USER
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_DETAIL_USER
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_ID_USER
import com.example.githubuserrev6.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var favoriteUserAdapter: UserGithubAdapter
    private lateinit var favoriteVM: FavoriteViewModel
    private lateinit var favoriteRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        favoriteRV = favoriteBinding.favoriteRv
        val myFavoriteRV = favoriteRV

        favoriteVM = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteVM.getFavoriteList()?.observe(this, {
            if (it != null) {
                val listFavorite = getListFavorite(it)
                favoriteUserAdapter.listUserGithub(listFavorite)
            }
        })

        favoriteUserAdapter = UserGithubAdapter(this)
        favoriteUserAdapter.notifyItemInserted(0)
        myFavoriteRV.adapter = favoriteUserAdapter
        myFavoriteRV.let { favoriteUserAdapter.notifyItemChanged(it.size) }
        myFavoriteRV.scrollToPosition(0)
        myFavoriteRV.setHasFixedSize(true)
        myFavoriteRV.layoutManager = GridLayoutManager(this, 2)

        favoriteUserAdapter.setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
            override fun setItemClicked(data: UserGithub) {
                val favoriteIntent =
                    Intent(this@FavoriteActivity, FavoriteUserDetailActivity::class.java)
                favoriteIntent.putExtra(EXTRA_DETAIL_USER, data.username)
                favoriteIntent.putExtra(EXTRA_ID_USER, data.id)
                favoriteIntent.putExtra(EXTRA_AVATAR_USER, data.avatar_url)
                startActivity(favoriteIntent)
            }
        })

    }

    private fun getListFavorite(data: List<FavoriteUserGithub>): ArrayList<UserGithub> {
        val listUser = ArrayList<UserGithub>()

        for (user in data) {
            val map = UserGithub(
                user.username,
                user.id,
                user.avatar_url
            )
            listUser.add(map)
        }
        return listUser
    }
}