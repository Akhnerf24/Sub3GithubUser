package com.example.githubuserrev6

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var daoFavoriteUser: DaoFavoriteUserGithub? = null
    private var databaseFavoriteUser: DatabaseFavoriteUserGithub.DatabaseFavoriteUser? =
        DatabaseFavoriteUserGithub.DatabaseFavoriteUser.getFavoriteInstance(application)

    init {
        daoFavoriteUser = databaseFavoriteUser?.daoFavoriteUser()
    }

    fun getFavoriteList(): LiveData<List<FavoriteUserGithub>>? = daoFavoriteUser?.getFavoriteUser()
}