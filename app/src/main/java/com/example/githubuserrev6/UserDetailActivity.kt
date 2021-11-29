package com.example.githubuserrev6

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserrev6.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityUserDetailBinding
    private lateinit var detailUserVM: UserDetailViewModel
    private lateinit var uNameUser: String
    private lateinit var imageUser: String
    private lateinit var idUser: String

    private fun showDetailUserGithub(DetailUserValue: Boolean){
        if (DetailUserValue){
            detailBinding.progressBar2.visibility = View.VISIBLE
        }else{
            detailBinding.progressBar2.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        uNameUser = intent.getStringExtra(EXTRA_DETAIL_USER).toString()
        idUser = intent.getStringExtra(EXTRA_ID_USER).toString()
        imageUser = intent.getStringExtra(EXTRA_AVATAR_USER).toString()

        detailUserVM = ViewModelProvider(this)[UserDetailViewModel::class.java]

        val backMain = detailBinding.backDetail
        backMain.setOnClickListener {
            super.onBackPressed()
        }

        val shareMain = detailBinding.shareDetail
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
        showDetailUserGithub(DetailUserValue = true)
        userGithubDetail()
    }

    private fun configFavorite() {
        val favoriteToggle = detailBinding.favoriteButton
        val check = detailUserVM.checkUser(uNameUser)
        (uNameUser.equals(check)).also { favoriteToggle.isChecked = it }
        favoriteToggle.setOnClickListener {
            if (favoriteToggle.isChecked) {
                detailUserVM.addFavoriteUser(uNameUser, idUser, imageUser)

            } else {
                detailUserVM.deleteFavorite(uNameUser)
            }
        }

    }

    private fun userGithubDetail() {
        detailUserVM.getUserDetail(this@UserDetailActivity, uNameUser)
        detailUserVM.getReturnUserDetail().observe(this, {
            if (it.isNotEmpty()) {
                showDetailUserGithub(DetailUserValue = false)
                detailBinding.nameDetail.text = it[0].name
                detailBinding.usernameDetail.text = it[0].username
                detailBinding.companyDetail.text = it[0].company
                detailBinding.locationDetail.text = it[0].location

                Glide.with(this)
                    .load(it[0].avatar_url)
                    .into(detailBinding.imageUser)

                val followerText = resources.getString(R.string.followers)
                val followingText = resources.getString(R.string.following)
                val repositoryText = resources.getString(R.string.repository)

                val tabLayout = detailBinding.detailTab
                val viewPager = detailBinding.detailViewPager
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

    companion object {
        const val EXTRA_DETAIL_USER = "extra_detail_user"
        const val EXTRA_ID_USER = "extra_id_user"
        const val EXTRA_AVATAR_USER = "extra_avatar_user"
    }
}