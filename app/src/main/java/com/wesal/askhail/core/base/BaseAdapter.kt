package com.wesal.askhail.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.mutual.LoadingVh

abstract class BaseAdapter<T,Y : BaseAdapter.BaseAdapterClickers<T>>(
    val list: MutableList<T?>,
    val callBack :Y?

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_PROG = 0
    }

    @LayoutRes
    abstract fun rowViewHolder(): Int
    abstract fun getViewHolder(view: View): RecyclerView.ViewHolder

    override fun getItemViewType(position: Int): Int {
        return if (list[position] != null) VIEW_ITEM else VIEW_PROG
    }
    interface BaseAdapterClickers<T>{
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(rowViewHolder(), parent, false);
                return getViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_loading, parent, false)
                LoadingVh(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseViewHolder<*>)
            (holder as? BaseViewHolder<T>)?.bind(list[position], position)

    }

    override fun getItemCount(): Int = list.size
    fun addMoreInList(listItems: List<T>?) {
        listItems?.let {
            val size = this.list.size
            list.addAll(it)
            val sizeNew = this.list.size
            notifyItemRangeChanged(size, sizeNew)
        }
    }

    fun addLoadingFooter() {
        list.add(null)
        notifyItemInserted(list.size - 1)
    }

    fun removeLoadingFooter() {
        val position = list.size - 1
        list.removeAt(position)
        notifyItemRemoved(list.size)
    }

    fun removeFromList(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
    }

}