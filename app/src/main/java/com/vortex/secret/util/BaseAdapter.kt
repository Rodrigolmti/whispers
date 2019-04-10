package com.vortex.secret.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

typealias OnClick<M> = (item: M) -> Unit

abstract class BaseAdapter<M> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val data: MutableList<M> = mutableListOf()

    fun addItems(items: List<M>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}

abstract class BaseViewHolder<M>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onClick(onClick: OnClick<M>)
}