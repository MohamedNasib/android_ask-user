package com.wesal.askhail.features.myAccount

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
import com.wesal.askhail.databinding.RowMyAccountBinding
import com.wesal.entities.models.AdvMediaModel

class AccountAdapter(private val list: MutableList<AccountModel?>,private val callBack: OnAccount?)
    : RecyclerView.Adapter<AccountAdapter.HolderView>() {

    //: BaseAdapter<AccountModel?, AccountAdapter.OnAccount>(list, callBack) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowMyAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<AccountModel>?) {
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
    inner class HolderView(private val itemBinding: RowMyAccountBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AccountModel?, position: Int) {
            model?.let {
                itemBinding.ivIcon.setImageResource(it.icon)
                itemBinding.tvTitle.text = it.text
                itemView.setOnClickListener { _->callBack?.onAccount(it) }
            }
        }

    }

    interface OnAccount : BaseAdapter.BaseAdapterClickers<AccountModel?> {
        fun onAccount(model: AccountModel)
    }
}