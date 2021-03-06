package com.vortex.secret.ui.app.comment

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.data.model.PostCommentModel
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.remote.AnalyticsEvents
import com.vortex.secret.ui.app.home.adapter.PostCommentAdapter
import com.vortex.secret.ui.base.BaseActivity
import com.vortex.secret.util.exceptions.NetworkError
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.hideKeyboard
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.activity_post_comment.*
import org.koin.android.viewmodel.ext.android.viewModel

const val POST_ID = "postId"

class PostCommentActivity : BaseActivity() {

    private val viewModel by viewModel<PostCommentViewModel>()
    private lateinit var adapter: PostCommentAdapter
    private lateinit var postModel: PostModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = ""
        }

        ivSend.setOnClickListener {
            if (validateField()) {
                val postCommentModel = PostCommentModel(comment = etComment.text.toString())
                window.decorView.rootView.hideKeyboard()
                analyticsManager.sendEvent(AnalyticsEvents.ADD_COMMENT.name)
                viewModel.updatePostComment(postModel, postCommentModel)
                etComment.text = null
            }
        }

        rvComments.layoutManager = LinearLayoutManager(this)
        adapter = PostCommentAdapter()
        rvComments.adapter = adapter

        intent.getStringExtra(POST_ID)?.let {
            viewModel.getPostById(it)
        }

        viewModel.responsePostModelLiveData.observe(this, Observer {
            it?.let { post ->

                postModel = post

                post.color?.let { color ->
                    tvBody.setBackgroundColor(ContextCompat.getColor(this, color))
                } ?: run {
                    tvBody.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                }

                post.body?.let { text ->
                    tvBody.visible()
                    tvBody.text = text
                }

                adapter.addItems(post.comments)
            }
        })

        viewModel.errorLiveData.observe(this, Observer { error ->
            when (error) {
                is NetworkError -> {
                    window.decorView.rootView.showSnackBar(getString(R.string.general_error_connection))
                }
                else -> {
                    error.message?.let { message -> window.decorView.rootView.showSnackBar(message) }
                }
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { isLoading ->
            if (isLoading) {
                rvComments.gone()
                tvBody.gone()
                viewInput.gone()
                ltLoading.visible()
            } else {
                ltLoading.gone()
                viewInput.visible()
                rvComments.visible()
                tvBody.visible()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun validateField(): Boolean {
        if (etComment.text.isEmpty()) {
            window.decorView.rootView.showSnackBar(getString(R.string.post_comment_error_fragment))
            return false
        }

        return true
    }
}
