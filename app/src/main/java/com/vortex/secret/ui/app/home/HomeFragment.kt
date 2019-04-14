package com.vortex.secret.ui.app.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.data.remote.AnalyticsEvents
import com.vortex.secret.ui.app.comment.POST_ID
import com.vortex.secret.ui.app.comment.PostCommentActivity
import com.vortex.secret.ui.app.home.adapter.PostAdapter
import com.vortex.secret.ui.base.BaseFragment
import com.vortex.secret.util.exceptions.EmptyDataError
import com.vortex.secret.util.exceptions.NetworkError
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var adapter: PostAdapter
    private var highlight = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        arguments?.let { bundle ->
            highlight = HomeFragmentArgs.fromBundle(bundle).highlight
            viewModel.updatePosts(highlight)
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.updatePosts(highlight)
        }

        viewModel.updatePostLiveDate.observe(this, Observer {
            it?.let { posts ->
                if (posts.isNotEmpty()) {
                    controlErrorVisibility(false)
                    adapter.addItems(posts)
                } else {
                    controlErrorVisibility(true)
                    adapter.clearData()
                }
            }
        })

        viewModel.errorLiveData.observe(this, Observer { error ->
            when (error) {
                is EmptyDataError -> {
                    controlErrorVisibility(showError = true)
                }
                is NetworkError -> {
                    view.showSnackBar(getString(R.string.general_error_connection))
                    controlErrorVisibility(showError = true)
                }
                else -> {
                    error.message?.let { view.showSnackBar(it) }
                }
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { isLoading ->
            if (isLoading) {
                controlErrorVisibility(false)
                ltLoading.visible()
                rvPosts.gone()
            } else {
                swipeRefresh.isRefreshing = false
                rvPosts.visible()
                ltLoading.gone()
            }
        })
    }

    private fun setupAdapter() {
        rvPosts.layoutManager = LinearLayoutManager(context)
        adapter = PostAdapter(
            onClickView = {},
            onClickLike = {
                analyticsManager.sendEvent(AnalyticsEvents.LIKE_POST.name)
                viewModel.updatePostLike(it)
            },
            onClickRemove = {
                analyticsManager.sendEvent(AnalyticsEvents.REMOVE_POST.name)
                viewModel.removePost(it)
            },
            onClickComment = { postModel ->
                val intent = Intent(context, PostCommentActivity::class.java)
                intent.putExtra(POST_ID, postModel.id)
                startActivity(intent)
            }
        )
        rvPosts.adapter = adapter
    }

    private fun controlErrorVisibility(showError: Boolean) {
        if (showError) {
            tvError.visible()
            ivError.visible()
        } else {
            tvError.gone()
            ivError.gone()
        }
    }
}
