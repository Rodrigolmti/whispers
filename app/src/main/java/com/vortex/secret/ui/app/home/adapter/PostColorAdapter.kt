package com.vortex.secret.ui.app.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vortex.secret.R
import com.vortex.secret.data.model.PostColor
import com.vortex.secret.ui.base.BaseAdapter
import com.vortex.secret.ui.base.BaseViewHolder
import com.vortex.secret.ui.base.OnClick
import com.vortex.secret.ui.custom.CustomCircleView
import java.util.*

class PostColorAdapter(
        private val items: List<PostColor>,
        private val onClick: OnClick<PostColor>
) : BaseAdapter<PostColor>() {

    private var selectedItems = ArrayList<Boolean>()

    init {
        items.forEach { _ ->
            selectedItems.add(false)
        }
        data.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(
                LayoutInflater.from(parent.context).inflate(R.layout.item_post_color, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as Item)
        item.bindData(data[position])
        item.onClick(onClick)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class Item(itemView: View) : BaseViewHolder<PostColor>(itemView) {

        private lateinit var item: PostColor

        override fun onClick(onClick: OnClick<PostColor>) {
            itemView.setOnClickListener {
                val position = items.indexOf(item)
                items.forEachIndexed { index, _ ->
                    selectedItems[index] = false
                }
                selectedItems[position] = !selectedItems[position]
                notifyDataSetChanged()
                onClick(item)
            }
        }

        fun bindData(item: PostColor) {

            val circle = itemView.findViewById<CustomCircleView>(R.id.cvColor)
            val imageCheck = itemView.findViewById<ImageView>(R.id.imCheck)

            imageCheck.visibility = if (selectedItems[items.indexOf(item)]) {
                View.VISIBLE
            } else {
                View.GONE
            }
            circle.updateColor(item.color)
            this.item = item
        }
    }
}