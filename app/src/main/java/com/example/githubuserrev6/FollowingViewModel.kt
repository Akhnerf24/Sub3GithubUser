package com.example.githubuserrev6

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {
    private val followingUserGithub = MutableLiveData<ArrayList<UserGithub>>()

    fun getFollowingUserGithub(context: Context, uNameUser: String) {
        val dataFollowingUser = ArrayList<UserGithub>()
        val connectAPI = AsyncHttpClient()
        val urlFollowingUserGithub = "https://api.github.com/users/$uNameUser/following"

        connectAPI.addHeader("Authorization", "token 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")
        connectAPI.addHeader("User-Agent", "request")

        connectAPI.get(urlFollowingUserGithub, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val resultFollowingUserGithub = String(responseBody)

                try {
                    val responseArrayFollowing = JSONArray(resultFollowingUserGithub)

                    for (f in 0 until responseArrayFollowing.length()) {
                        val followingUser = responseArrayFollowing.getJSONObject(f)
                        val userGithub = UserGithub()

                        val username = followingUser.getString("login")
                        val avatar = followingUser.getString("avatar_url")

                        userGithub.username = username
                        userGithub.avatar_url = avatar

                        dataFollowingUser.add(userGithub)
                    }
                    followingUserGithub.postValue(dataFollowingUser)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Error!! Read : ${e.message}")
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Error!! Read : $errorMessage")
            }

        })
    }

    fun getReturnUserFollowers(): MutableLiveData<ArrayList<UserGithub>> {
        return followingUserGithub
    }
}