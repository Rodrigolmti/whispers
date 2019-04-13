package com.vortex.secret.ui.app.comment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.data.model.PostCommentModel
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.ui.app.home.adapter.PostCommentAdapter
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.activity_post_comment.*
import org.koin.android.viewmodel.ext.android.viewModel

class PostCommentActivity : AppCompatActivity() {

    private val viewModel by viewModel<PostCommentViewModel>()
    private lateinit var adapter: PostCommentAdapter
    private lateinit var postModel: PostModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment)

        ivSend.setOnClickListener {
            if (validateField()) {
                val postCommentModel = PostCommentModel(comment = etComment.text.toString())
                viewModel.updatePostComment(postModel, postCommentModel)
                etComment.text = null
            }
        }

        rvComments.layoutManager = LinearLayoutManager(this)
        adapter = PostCommentAdapter()
        rvComments.adapter = adapter

        intent.getStringExtra("postId")?.let {
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
            error.message?.let { window.decorView.rootView.showSnackBar(it) }
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


    private fun validateField(): Boolean {
        if (etComment.text.isEmpty()) {
            window.decorView.rootView.showSnackBar(getString(R.string.post_comment_error_fragment))
            return false
        }

        return true
    }
}
