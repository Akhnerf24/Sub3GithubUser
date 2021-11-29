package com.example.githubuserrev6

import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuserrev6.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment(R.layout.fragment_followers) {
    private var followersFragmentBinding: FragmentFollowersBinding? = null
    private lateinit var followersFragmentAdapter: UserGithubAdapter
    private lateinit var followersVM: FollowersViewModel
    private lateinit var uNameUser: String

    private fun showFollowersUserGithub(followersValue: Boolean) {
        if (followersValue) {
            followersFragmentBinding?.followersProgressBar?.visibility = View.VISIBLE
        } else {
            followersFragmentBinding?.followersProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uNameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        followersFragmentBinding = FragmentFollowersBinding.bind(view)

        val followersRV = followersFragmentBinding?.followersRv
        followersFragmentAdapter = UserGithubAdapter(requireActivity())
        followersRV?.let { followersFragmentAdapter.notifyItemChanged(it.size) }
        followersFragmentAdapter.notifyItemInserted(0)
        followersRV?.scrollToPosition(0)
        followersRV?.setHasFixedSize(true)
        followersRV?.layoutManager = GridLayoutManager(activity, 2)
        followersRV?.adapter = followersFragmentAdapter

        showFollowersUserGithub(true)

        followersVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowersViewModel::class.java]
        this.context?.let { followersVM.getFollowersUserGithub(it, uNameUser) }
        followersVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)
                    showFollowersUserGithub(false)
                } else {
                    followersFragmentAdapter.listUserGithub(it)

                    showNull(false)
                    showFollowersUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        followersFragmentBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        followersFragmentBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            followersFragmentBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            followersFragmentBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }
}