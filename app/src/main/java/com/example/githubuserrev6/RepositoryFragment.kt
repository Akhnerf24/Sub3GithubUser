package com.example.githubuserrev6

import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubuserrev6.databinding.FragmentRepositoryBinding

class RepositoryFragment : Fragment(R.layout.fragment_repository) {
    private var repositoryFragmentBinding: FragmentRepositoryBinding? = null
    private lateinit var repositoryFragmentAdapter: UserRepositoryAdapter
    private lateinit var repositoryVM: RepositoryViewModel
    private lateinit var uNameUser: String

    private fun showRepositoryUserGithub(repositoryValue: Boolean) {
        if (repositoryValue) {
            repositoryFragmentBinding?.repositoryProgressBar?.visibility = View.VISIBLE
        } else {
            repositoryFragmentBinding?.repositoryProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uNameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        repositoryFragmentBinding = FragmentRepositoryBinding.bind(view)

        val repositoryRV = repositoryFragmentBinding?.repositoryRv
        repositoryFragmentAdapter = UserRepositoryAdapter()
        repositoryRV?.size?.let { repositoryFragmentAdapter.notifyItemChanged(it) }

        repositoryRV?.setHasFixedSize(true)
        repositoryRV?.layoutManager = GridLayoutManager(activity, 2)
        repositoryRV?.adapter = repositoryFragmentAdapter

        showRepositoryUserGithub(true)

        repositoryVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RepositoryViewModel::class.java)
        this.context?.let { repositoryVM.getRepositoryUserGithub(it, uNameUser) }
        repositoryVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)

                    showRepositoryUserGithub(false)
                } else {
                    repositoryFragmentAdapter.listRepositoryUserGithub(it)

                    showNull(false)
                    showRepositoryUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        repositoryFragmentBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        repositoryFragmentBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            repositoryFragmentBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            repositoryFragmentBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }

}