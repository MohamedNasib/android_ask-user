package com.wesal.askhail.features.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowPagesBinding
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.FixedPageModel

class FixedPagesAdapter (private val list: MutableList<FixedPageModel?>,private val callBack: OnFixedPage?)
    : RecyclerView.Adapter<FixedPagesAdapter.HolderView>() {

    //: BaseAdapter<FixedPageModel?, FixedPagesAdapter.OnFixedPage>(list, callBack) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowPagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<FixedPageModel>?) {
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
    inner class HolderView(private val itemBinding: RowPagesBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: FixedPageModel?, position: Int) {
            model?.let {
                itemBinding.tvPageSlugName?.text = it.fixedPageTitle
                itemView.setOnClickListener { _->callBack?.onFixedPage(it) }
            }
        }

    }


    interface OnFixedPage : BaseAdapter.BaseAdapterClickers<FixedPageModel?> {
        fun onFixedPage(model: FixedPageModel)
    }
}