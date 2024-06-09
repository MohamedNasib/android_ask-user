package com.wesal.askhail.features.highlightAdvert.highlightStepInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.databinding.RowHighlightBinding
import com.wesal.entities.models.AdvMediaModel

class HighlightAdapter(private val list: MutableList<String?>,private val callBack: OnBusinessSection?)
    : RecyclerView.Adapter<HighlightAdapter.HolderView>() {
    //: BaseAdapter<String?, HighlightAdapter.OnBusinessSection>(list, callBack) {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<String>?) {
        listItems?.let {
            val size = this.list.size
            list.addAll(it)
            val sizeNew = this.list.size
            notifyItemRangeChanged(size, sizeNew)
        }
    }
    override fun getItemCount(): Int = list.size
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

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        holder.bind(list[position],position)
    }
    inner class HolderView(private val itemBinding: RowHighlightBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: String?, position: Int) {
            model?.let {
                itemBinding.tvHighlight.text = it
            }
        }

    }



    interface OnBusinessSection : BaseAdapter.BaseAdapterClickers<String?> {
        fun onBusinessSection(model: String)
    }
}