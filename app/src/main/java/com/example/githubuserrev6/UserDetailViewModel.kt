package com.example.githubuserrev6

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val detailUserGithub = MutableLiveData<ArrayList<UserGithub>>()
    private var daoFavoriteUser: DaoFavoriteUserGithub? = null
    private var databaseFavoriteUser: DatabaseFavoriteUserGithub.DatabaseFavoriteUser? = null

    init {
        databaseFavoriteUser =
            DatabaseFavoriteUserGithub.DatabaseFavoriteUser.getFavoriteInstance(application)
        daoFavoriteUser = databaseFavoriteUser?.daoFavoriteUser()
    }

    fun getUserDetail(context: Context, uNameUser: String) {
        val dataDetailUser = ArrayList<UserGithub>()
        val connectAPI = AsyncHttpClient()
        val urlDetailUserGithub = "https://api.github.com/users/$uNameUser"

        connectAPI.addHeader("Authorization", "token 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")
        connectAPI.addHeader("User-Agent", "request")

        if (uNameUser.isNotEmpty()) {
            connectAPI.get(urlDetailUserGithub, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val resultUserDetail = String(responseBody)

                    try {
                        val responseUserObject = JSONObject(resultUserDetail)
                        val userGithub = UserGithub()

                        fun JSONObject.getNullableString(
                            name: String,
                            fallback: String? = null
                        ): String? {
                            return if (this.has(name) && !this.isNull(name)) {
                                this.getString(name)
                            } else {
                                fallback
                            }
                        }

                        val image = responseUserObject.getString("avatar_url")
                        val name =
                            responseUserObject.getNullableString("name", "- User not set name -")
                        val username = responseUserObject.getString("login")
                        val company = responseUserObject.getNullableString(
                            "company",
                            "- User not set company -"
                        )
                        val location = responseUserObject.getNullableString(
                            "location",
                            "- User not set location -"
                        )
                        val id = responseUserObject.getString("id")
                        val followers = responseUserObject.getInt("followers")
                        val following = responseUserObject.getInt("following")
                        val repository = responseUserObject.getInt("public_repos")

                        userGithub.avatar_url = image
                        userGithub.name = name
                        userGithub.username = username
                        userGithub.company = company
                        userGithub.location = location
                        userGithub.id = id
                        userGithub.followers = followers
                        userGithub.following = following
                        userGithub.repository = repository


                        dataDetailUser.add(userGithub)
                        detailUserGithub.postValue(dataDetailUser)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Failed!! Read : ${e.message}")
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    val errorMessage = when (statusCode) {
                        400 -> "Bad Request! Code : $statusCode"
                        401 -> "Bad Request! Code : $statusCode"
                        403 -> "Bad Request! Code : $statusCode"
                        404 -> "Bad Request! Code : $statusCode"

                        else -> error?.message + "! Code : $statusCode"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Error!! Read : $errorMessage")
                }

            })
        }
    }

    fun getReturnUserDetail(): MutableLiveData<ArrayList<UserGithub>> {
        return detailUserGithub
    }

    fun addFavoriteUser(uNameUser: String, id: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myFav = FavoriteUserGithub(
                uNameUser,
                id,
                image
            )
            daoFavoriteUser?.addFavoriteUser(myFav)
        }
    }

    fun deleteFavorite(uNameUser: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val deleteUser = FavoriteUserGithub(
                uNameUser
            )
            daoFavoriteUser?.delete(deleteUser)
        }
    }

    fun checkUser(uNameUser: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val checkUser = FavoriteUserGithub(
                uNameUser
            )
            daoFavoriteUser?.checkFavoriteUser(checkUser.username)
        }
    }

}