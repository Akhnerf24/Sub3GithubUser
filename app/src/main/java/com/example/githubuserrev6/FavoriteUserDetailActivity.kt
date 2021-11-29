package com.example.githubuserrev6

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_AVATAR_USER
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_DETAIL_USER
import com.example.githubuserrev6.UserDetailActivity.Companion.EXTRA_ID_USER
import com.example.githubuserrev6.databinding.ActivityFavoriteUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteUserDetailActivity : AppCompatActivity() {
    private lateinit var detailFavoriteBinding: ActivityFavoriteUserDetailBinding
    private lateinit var detailFavoriteVM: UserDetailViewModel
    private lateinit var uNameUser: String
    private lateinit var imageUser: String
    private lateinit var idUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailFavoriteBinding = ActivityFavoriteUserDetailBinding.inflate(layoutInflater)
        setContentView(detailFavoriteBinding.root)

        uNameUser = intent.getStringExtra(EXTRA_DETAIL_USER).toString()
        idUser = intent.getStringExtra(EXTRA_ID_USER).toString()
        imageUser = intent.getStringExtra(EXTRA_AVATAR_USER).toString()

        detailFavoriteVM = ViewModelProvider(this).get(UserDetailViewModel::class.java)

        val backMain = detailFavoriteBinding.backDetail
        backMain.setOnClickListener {
            super.onBackPressed()
        }

        val shareMain = detailFavoriteBinding.shareDetail
        shareMain.setOnClickListener {
            val shareItem: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, uNameUser)
                type = "text/plain"
            }

            val shareMainIntent = Intent.createChooser(shareItem, null)
            startActivity(shareMainIntent)
        }

        configFavorite()
        userGithubDetail()
    }

    private fun configFavorite() {
        val favoriteToggle = detailFavoriteBinding.favoriteButton
        favoriteToggle.isChecked = true
        favoriteToggle.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            if (isChecked) {
                detailFavoriteVM.addFavoriteUser(uNameUser, idUser, imageUser)
            } else {
                detailFavoriteVM.deleteFavorite(uNameUser)
            }
        }
    }

    private fun userGithubDetail() {
        detailFavoriteVM.getUserDetail(this, uNameUser)
        detailFavoriteVM.getReturnUserDetail().observe(this, {
            if (it.isNotEmpty()) {
                detailFavoriteBinding.nameDetail.text = it[0].name
                detailFavoriteBinding.usernameDetail.text = it[0].username
                detailFavoriteBinding.companyDetail.text = it[0].company
                detailFavoriteBinding.locationDetail.text = it[0].location

                Glide.with(this)
                    .load(it[0].avatar_url)
                    .into(detailFavoriteBinding.imageUser)

                val followerText = resources.getString(R.string.followers)
                val followingText = resources.getString(R.string.following)
                val repositoryText = resources.getString(R.string.repository)

                val tabLayout = detailFavoriteBinding.detailTab
                val viewPager = detailFavoriteBinding.detailViewPager
                val intentUserGithub = intent.getStringExtra(EXTRA_DETAIL_USER)
                val detailBundle = Bundle()
                detailBundle.putString(EXTRA_DETAIL_USER, intentUserGithub)

                val tabAdapter = SectionPageAdapter(this, detailBundle)

                viewPager.adapter = tabAdapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = StringBuilder("${it[0].followers}\n$followerText")
                        }
                        1 -> {
                            tab.text = StringBuilder("${it[0].following}\n$followingText")
                        }
                        2 -> {
                            tab.text = StringBuilder("${it[0].repository}\n$repositoryText")
                        }
                    }
                }.attach()
            }
        })
    }
}