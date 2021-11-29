package com.example.githubuserrev6

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuserrev6.DatabaseFavoriteUserGithub.DatabaseFavoriteUser.Companion.TABLE_VERSION

class DatabaseFavoriteUserGithub {
    @Database(entities = [FavoriteUserGithub::class], version = TABLE_VERSION, exportSchema = false)

    abstract class DatabaseFavoriteUser : RoomDatabase() {
        abstract fun daoFavoriteUser(): DaoFavoriteUserGithub

        companion object {
            const val TABLE_NAME = "table_favorite_user"
            const val TABLE_VERSION = 1

            @Volatile
            var FAVORITE_INSTANCE: DatabaseFavoriteUser? = null

            fun getFavoriteInstance(context: Context): DatabaseFavoriteUser? {
                if (FAVORITE_INSTANCE == null) {
                    synchronized(DatabaseFavoriteUser::class) {
                        FAVORITE_INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseFavoriteUser::class.java,
                            TABLE_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
                return FAVORITE_INSTANCE
            }
        }
    }
}