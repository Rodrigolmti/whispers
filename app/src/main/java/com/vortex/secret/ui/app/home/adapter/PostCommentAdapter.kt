package com.vortex.secret.ui.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vortex.secret.R
import com.vortex.secret.data.model.PostCommentModel
import com.vortex.secret.ui.base.BaseAdapter
import com.vortex.secret.ui.base.BaseViewHolder
import com.vortex.secret.ui.base.OnClick
import com.vortex.secret.ui.custom.CustomCircleView

class PostCommentAdapter : BaseAdapter<PostCommentModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as Item)
        item.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class Item(itemView: View) : BaseViewHolder<PostCommentModel>(itemView) {

        private lateinit var item: PostCommentModel

        override fun onClick(onClick: OnClick<PostCommentModel>) {
            itemView.setOnClickListener { onClick(item) }
        }

        fun bindData(item: PostCommentModel) {

            val textView = itemView.findViewById<TextView>(R.id.tvComment)
            val cvColor = itemView.findViewById<CustomCircleView>(R.id.cvColor)

            item.comment?.let { textView.text = it }
            cvColor.updateColor(R.color.geyser)

            this.item = item
        }
    }
}