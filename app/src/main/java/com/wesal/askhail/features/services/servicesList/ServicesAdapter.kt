package com.wesal.askhail.features.services.servicesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.databinding.RowServicesBinding
import com.wesal.entities.models.ServicesModel

class ServicesAdapter (private val list: MutableList<ServicesModel?>,private val callBack: OnServices?)
    : RecyclerView.Adapter<ServicesAdapter.HolderView>() {

    //: BaseAdapter<ServicesModel?, ServicesAdapter.OnServices>(list, callBack) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<ServicesModel>?) {
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
    inner class HolderView(private val itemBinding: RowServicesBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: ServicesModel?, position: Int) {
            model?.let {
                itemBinding.tvName.text= it.serviceTitle
                itemBinding.tvDate.text = it.serviceCustomAddedDate
                itemBinding.ivDownload.setOnClickListener { _->callBack?.onServices(it) }
            }
        }

    }


    interface OnServices : BaseAdapter.BaseAdapterClickers<ServicesModel?> {
        fun onServices(model: ServicesModel)
    }
}