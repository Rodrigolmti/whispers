package com.vortex.secret.ui.app.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vortex.secret.R
import com.vortex.secret.data.model.PostCommentModel
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.ui.app.home.adapter.PostCommentAdapter
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.showSnackBar
import com.vortex.secret.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_post_comment.*
import org.koin.android.viewmodel.ext.android.viewModel

class PostCommentFragment : Fragment() {

    private val viewModel by viewModel<PostCommentViewModel>()
    private lateinit var adapter: PostCommentAdapter
    private lateinit var postModel: PostModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivSend.setOnClickListener {
            if (validateField()) {
                val postCommentModel = PostCommentModel(comment = etComment.text.toString())
                viewModel.updatePostComment(postModel, postCommentModel)
                etComment.text = null
            }
        }

        rvComments.layoutManager = LinearLayoutManager(context)
        adapter = PostCommentAdapter()
        rvComments.adapter = adapter

        arguments?.let { args ->
            val arguments = PostCommentFragmentArgs.fromBundle(args)
            viewModel.getPostById(arguments.postId)
        }

        viewModel.responsePostModelLiveData.observe(this, Observer {
            it?.let { post ->

                postModel = post

                post.color?.let { color ->
                    tvBody.setBackgroundColor(ContextCompat.getColor(view.context, color))
                } ?: run {
                    tvBody.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
                }

                post.body?.let { text ->
                    tvBody.visible()
                    tvBody.text = text
                }

                adapter.addItems(post.comments)
            }
        })

        viewModel.errorLiveData.observe(this, Observer { error ->
            error.message?.let { view.showSnackBar(it) }
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

    private fun validateField() : Boolean {
        if (etComment.text.isEmpty()) {
            view?.showSnackBar(getString(R.string.post_comment_error_fragment))
            return false
        }

        return true
    }
}
