package com.wesal.askhail.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.databinding.RowHomeRealstateBinding
import com.wesal.entities.models.SectionModel

class HomeRealStateAdapter(private val list: MutableList<SectionModel?>, private val callBack:  OnRealStateSection?)
    : RecyclerView.Adapter<HomeRealStateAdapter.HolderView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowHomeRealstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<SectionModel>?) {
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
    inner class HolderView(private val itemBinding: RowHomeRealstateBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: SectionModel?, position: Int) {
            model?.let {
                itemBinding.ivRealImage?.loadServerImage(it.sectionImage)
                itemBinding.tvSecName?.text = it.sectionName
                itemView.setOnClickListener {_-> callBack?.onRealStateSection(it) }
            }
        }

    }
    interface OnRealStateSection : BaseAdapter.BaseAdapterClickers<SectionModel?> {
        fun onRealStateSection(model: SectionModel)
    }
}
