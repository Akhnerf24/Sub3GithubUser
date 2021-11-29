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

class RepositoryViewModel : ViewModel() {
    private val repositoryUserGithub = MutableLiveData<ArrayList<UserGithub>>()

    fun getRepositoryUserGithub(context: Context, uNameUser: String) {
        val dataRepositoryUser = ArrayList<UserGithub>()
        val connectAPI = AsyncHttpClient()
        val urlRepositoryUserGithub = "https://api.github.com/users/$uNameUser/repos"

        connectAPI.addHeader("Authorization", "token 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")
        connectAPI.addHeader("User-Agent", "request")

        connectAPI.get(urlRepositoryUserGithub, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val resultFollowingUserGithub = String(responseBody)

                try {
                    val responseArrayRepository = JSONArray(resultFollowingUserGithub)

                    for (r in 0 until responseArrayRepository.length()) {
                        val repositoryUser = responseArrayRepository.getJSONObject(r)
                        val userGithub = UserGithub()

                        val repositoryName = repositoryUser.getString("name")

                        userGithub.repositoryName = repositoryName

                        dataRepositoryUser.add(userGithub)
                    }
                    repositoryUserGithub.postValue(dataRepositoryUser)
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
        return repositoryUserGithub
    }
}