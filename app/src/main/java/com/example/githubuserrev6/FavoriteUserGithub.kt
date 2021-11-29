package com.example.githubuserrev6

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubuserrev6.DatabaseFavoriteUserGithub.DatabaseFavoriteUser.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class FavoriteUserGithub(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String = "",

    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "avatar_url")
    val avatar_url: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "company")
    val company: String = "",

    @ColumnInfo(name = "followers")
    val followers: Int = 0,

    @ColumnInfo(name = "following")
    val following: Int = 0,

    @ColumnInfo(name = "repository")
    val repository: Int = 0,

    @ColumnInfo(name = "repository_name")
    val repositoryName: String = ""
)
