package com.example.githubuserrev6

import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuserrev6.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment(R.layout.fragment_following) {
    private var followingFragmentBinding: FragmentFollowingBinding? = null
    private lateinit var followingFragmentAdapter: UserGithubAdapter
    private lateinit var followingVM: FollowingViewModel
    private lateinit var uNameUser: String

    private fun showFollowingUserGithub(followingValue: Boolean) {
        if (followingValue) {
            followingFragmentBinding?.followingProgressBar?.visibility = View.VISIBLE
        } else {
            followingFragmentBinding?.followingProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uNameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        followingFragmentBinding = FragmentFollowingBinding.bind(view)

        val followingRV = followingFragmentBinding?.followingRv
        followingFragmentAdapter = UserGithubAdapter(requireActivity())
        followingRV?.size?.let { followingFragmentAdapter.notifyItemChanged(it) }

        followingRV?.setHasFixedSize(true)
        followingRV?.layoutManager = GridLayoutManager(activity, 2)
        followingRV?.adapter = followingFragmentAdapter

        showFollowingUserGithub(true)

        followingVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]
        this.context?.let { followingVM.getFollowingUserGithub(it, uNameUser) }
        followingVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)

                    showFollowingUserGithub(false)
                } else {
                    followingFragmentAdapter.listUserGithub(it)

                    showNull(false)
                    showFollowingUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        followingFragmentBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        followingFragmentBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            followingFragmentBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            followingFragmentBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }
}