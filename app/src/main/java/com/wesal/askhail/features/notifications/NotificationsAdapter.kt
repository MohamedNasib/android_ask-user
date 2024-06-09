package com.wesal.askhail.features.notifications

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
import com.wesal.askhail.databinding.RowNotificationBinding
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.NotificationsModel

class NotificationsAdapter(private val list: MutableList<NotificationsModel?>,
                           private val callBack: OnNotification?)
    : RecyclerView.Adapter<NotificationsAdapter.HolderView>() {
    //: BaseAdapter<NotificationsModel?, NotificationsAdapter.OnNotification>(list, callBack) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<NotificationsModel>?) {
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
    inner class HolderView(private val itemBinding: RowNotificationBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: NotificationsModel?, position: Int) {
            model?.let {
                itemBinding.tvNotificationTitle.text = it.notifyText
                itemBinding.tvBody.text = it.notifyTypeTitle
                itemBinding.tvDate.text = it.notifyCustomDate

                itemView.setOnClickListener {_-> callBack?.onNotification(it) }
            }
        }

    }


    interface OnNotification : BaseAdapter.BaseAdapterClickers<NotificationsModel?> {
        fun onNotification(model: NotificationsModel)
    }
}