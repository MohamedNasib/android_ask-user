package com.wesal.askhail.features.advertDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.databinding.RowAdvertSpecificationBinding
import com.wesal.entities.models.AdvertSpecificationModel

class AdvertSpecificAdapter (private val list: MutableList<AdvertSpecificationModel?>,private val callBack: OnSpecification?)
    : RecyclerView.Adapter<AdvertSpecificAdapter.HolderView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowAdvertSpecificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<AdvertSpecificationModel>?) {
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
    inner class HolderView(private val itemBinding: RowAdvertSpecificationBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvertSpecificationModel?, position: Int) {
            model?.let {
                itemBinding.tvTopText?.text= it.specificationSectionFeature.featureName
                itemBinding.tvBottomText?.text= it.specificationAnswer
            }
        }

    }

    interface OnSpecification : BaseAdapter.BaseAdapterClickers<AdvertSpecificationModel?> {
        fun onSpecification(model: AdvertSpecificationModel)
    }
}
