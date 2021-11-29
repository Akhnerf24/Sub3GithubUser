package com.example.githubuserrev6

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserrev6.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var listUserRV: RecyclerView
    private lateinit var userGithubVM: UserSearchViewModel
    private lateinit var searchUserAdapter: UserGithubAdapter

    private var clickedValue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        showTitleAndSearchButton(true)

        listUserRV = mainBinding.rvUserGithub
        listUserRV.setHasFixedSize(true)

        listUserRV.layoutManager = GridLayoutManager(this, 2)
        searchUserAdapter = UserGithubAdapter(this)
        listUserRV.adapter = searchUserAdapter
        searchUserAdapter.notifyItemChanged(listUserRV.size)
        searchUserAdapter.setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
            override fun setItemClicked(data: UserGithub) {
                val searchUserIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                searchUserIntent.putExtra(UserDetailActivity.EXTRA_DETAIL_USER, data.username)
                searchUserIntent.putExtra(UserDetailActivity.EXTRA_ID_USER, data.id)
                searchUserIntent.putExtra(UserDetailActivity.EXTRA_AVATAR_USER, data.avatar_url)
                startActivity(searchUserIntent)
            }
        })

        val preferences = ThemePreferenceSetting.getInstance(dataStore)
        val themeButton = mainBinding.myTheme

        userGithubVM = ViewModelProvider(
            this,
            MainViewModelFactory(preferences)
        )[UserSearchViewModel::class.java]
        userGithubVM.getReturnUserGithub().observe(this, {
            if (it != null) {
                if (it.isEmpty()) {
                    showProgressBar(true)
                    showUnknown(true)
                    showEditText(true)
                    showTitleAndSearchButton(false)

                    searchUserAdapter.listUserGithub(arrayListOf())
                } else {
                    showProgressBar(false)
                    showUnknown(false)
                    showEditText(false)
                    showTitleAndSearchButton(true)

                    searchUserAdapter.listUserGithub(it)

                }
            }
        })
        userGithubVM.getThemeConfig().observe(this, { darkTheme: Boolean ->
            if (darkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                themeButton.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeButton.isChecked = false
            }
        })

        val searchingButton = mainBinding.searchButton
        searchingButton.setOnClickListener {
            showTitleAndSearchButton(false)
        }

        myExtendedFloatingButton()

    }

    private fun myExtendedFloatingButton() {
        val floatingButton = mainBinding.extendedFloatingButton

        floatingButton.setOnClickListener {
            configFloatingButton(clickedValue)

            clickedValue = !clickedValue
        }
    }

    private fun configFloatingButton(clicked: Boolean) {
        val favoriteButton = mainBinding.myFavorite

        if (!clicked) {
            favoriteButton.apply {
                visibility = View.VISIBLE
                isClickable = true

                setOnClickListener {
                    val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(favoriteIntent)
                }
            }
        } else {
            favoriteButton.apply {
                visibility = View.INVISIBLE
                isClickable = false
            }
        }
    }

    override fun onBackPressed() {
        if (clickedValue) {

            super.onBackPressed()
            return
        }
        this.clickedValue = true
        val exitText = resources.getText(R.string.exit)
        Toast.makeText(this, exitText, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ clickedValue = false }, 2000)
    }

    private fun showProgressBar(progressValue: Boolean) {
        mainBinding.searchProgressBar.visibility = if (progressValue) View.VISIBLE else View.GONE
    }

    private fun showUnknown(unknownValue: Boolean) {
        mainBinding.unknownImage.visibility = if (unknownValue) View.VISIBLE else View.INVISIBLE
        mainBinding.unknownText.visibility = if (unknownValue) View.VISIBLE else View.INVISIBLE
    }

    private fun showTitleAndSearchButton(visibleValue: Boolean) {
        val themeButton = mainBinding.myTheme
        if (visibleValue) {
            mainBinding.searchButton.visibility = View.VISIBLE
            mainBinding.titleMainHeader.visibility = View.VISIBLE

            themeButton.apply {
                visibility = View.VISIBLE
                isClickable = true

                setOnCheckedChangeListener { _: CompoundButton?, checked ->
                    userGithubVM.saveThemeConfig(checked)
                }
            }

            showEditText(false)
        } else {
            mainBinding.searchButton.visibility = View.INVISIBLE
            mainBinding.titleMainHeader.visibility = View.INVISIBLE

            themeButton.apply {
                visibility = View.INVISIBLE
                isClickable = false
            }

            showEditText(true)
        }
    }

    private fun showEditText(editTextValue: Boolean) {
        if (editTextValue) {
            val searchUserManager = this.getSystemService(SEARCH_SERVICE) as SearchManager
            mainBinding.searchEditText.visibility = View.VISIBLE
            mainBinding.searchEditText.setOnQueryTextListener(this)
            mainBinding.searchEditText.setSearchableInfo(
                searchUserManager.getSearchableInfo(
                    componentName
                )
            )
        } else {
            mainBinding.searchEditText.visibility = View.INVISIBLE
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        this.applicationContext?.let { userGithubVM.getUserGithub(it, query) }

        if (query.isEmpty())
            searchUserAdapter.listUserGithub(arrayListOf())

        showProgressBar(true)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}