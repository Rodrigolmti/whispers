package com.vortex.secret.ui.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vortex.secret.R
import com.vortex.secret.data.UserSession
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.util.BaseAdapter
import com.vortex.secret.util.BaseViewHolder
import com.vortex.secret.util.OnClick
import com.vortex.secret.util.extensions.gone
import com.vortex.secret.util.extensions.visible

class PostAdapter(
        private val onClickView: OnClick<PostModel>,
        private val onClickLike: OnClick<PostModel>,
        private val onClickRemove: OnClick<PostModel>,
        private val onClickComment: OnClick<PostModel>
) : BaseAdapter<PostModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
                LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as Item)
        item.setActionClicks(onClickLike, onClickRemove, onClickComment)
        item.bindData(data[position])
        item.onClick(onClickView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class Item(itemView: View) : BaseViewHolder<PostModel>(itemView) {

        private lateinit var onClickComment: OnClick<PostModel>
        private lateinit var onClickRemove: OnClick<PostModel>
        private lateinit var onClickLike: OnClick<PostModel>
        private lateinit var item: PostModel

        override fun onClick(onClick: OnClick<PostModel>) {
            itemView.setOnClickListener { onClick(item) }
        }

        fun setActionClicks(
                onClickLike: OnClick<PostModel>,
                onClickRemove: OnClick<PostModel>,
                onClickComment: OnClick<PostModel>
        ) {
            this.onClickComment = onClickComment
            this.onClickRemove = onClickRemove
            this.onClickLike = onClickLike
        }

        fun bindData(item: PostModel) {

            val container = itemView.findViewById<View>(R.id.container)
            val textView = itemView.findViewById<TextView>(R.id.tvBody)

            val ivLikes = itemView.findViewById<ImageView>(R.id.ivLikes)
            val tvLikes = itemView.findViewById<TextView>(R.id.tvLikes)

            val ivComments = itemView.findViewById<ImageView>(R.id.ivComments)
            val tvComments = itemView.findViewById<TextView>(R.id.tvComments)

            val ivRemove = itemView.findViewById<ImageView>(R.id.ivRemove)
            if (item.authorId == UserSession.userId) {
                ivRemove.setOnClickListener { onClickRemove(item) }
                ivRemove.visible()
            } else {
                ivRemove.gone()
            }

            item.color?.let {
                container.setBackgroundColor(ContextCompat.getColor(itemView.context, it))
            } ?: run {
                container.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            }
            item.body?.let { textView.text = it }

            tvComments.text = item.comments.size.toString()
            tvLikes.text = item.likesCount.toString()

            ivComments.setOnClickListener { onClickComment(item) }
            ivLikes.setOnClickListener { onClickLike(item) }

            this.item = item
        }
    }
}