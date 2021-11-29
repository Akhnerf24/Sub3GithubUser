package com.example.githubuserrev6

import android.os.Parcelable
import androidx.room.Entity
import com.example.githubuserrev6.DatabaseFavoriteUserGithub.DatabaseFavoriteUser.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class UserGithub(
    var username: String = "-",
    var id: String = "-",
    var avatar_url: String = "",
    var name: String? = "-",
    var location: String? = "-",
    var company: String? = "-",
    var followers: Int? = 0,
    var following: Int? = 0,
    var repository: Int = 0,
    var repositoryName: String? = "-"
) : Parcelable