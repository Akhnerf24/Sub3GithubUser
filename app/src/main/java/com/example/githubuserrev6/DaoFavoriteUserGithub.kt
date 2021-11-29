package com.example.githubuserrev6

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserrev6.DatabaseFavoriteUserGithub.DatabaseFavoriteUser.Companion.TABLE_NAME

@Dao
interface DaoFavoriteUserGithub {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteUser(userGithub: FavoriteUserGithub)

    @Query("SELECT * FROM $TABLE_NAME")
    fun showFavoriteUser(): Cursor

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFavoriteUser(): LiveData<List<FavoriteUserGithub>>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME WHERE $TABLE_NAME.username = :username")
    suspend fun checkFavoriteUser(username: String): Int

    @Delete
    fun delete(userGithub: FavoriteUserGithub)

}