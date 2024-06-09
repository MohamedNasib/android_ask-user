package com.wesal.askhail.features.subSectionsItems.ordersList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.invisible
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowJobBinding
import com.wesal.askhail.databinding.RowOrdersBinding
import com.wesal.entities.models.AdvertSpecificationModel
import com.wesal.entities.models.OrdersModel

class OrdersListAdapter(
    private val list: MutableList<OrdersModel?>,
    private val callBack: OnOrderClicked
)

    : RecyclerView.Adapter<OrdersListAdapter.HolderView>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding =
            RowOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }

    fun addMoreInList(listItems: List<OrdersModel>?) {
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
        holder.bind(list[position], position)
    }

    inner class HolderView(private val itemBinding: RowOrdersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: OrdersModel?, position: Int) {
            model?.let {
                itemView.setOnClickListener { _ -> callBack.onOrderClicked(it) }
                itemBinding.tvOrderMakerName.text =
                    "${it.orderAdvertiserName} ${itemView.context.getString(R.string.ordering)}"
                itemBinding.tvOrderDate.text = it.orderCustomDate
                itemBinding.tvOrderDetails.text = it.orderTitle
                itemBinding.tvOrderPrice.text =
                    "${itemView.context.getString(R.string.less_than)} ${it.orderPrice} ${
                        itemView.context.getString(R.string.sar)
                    }"
                if (it.orderDateStatus.isEmpty()) {
                    itemBinding.tvIsNew.invisible()
                } else {
                    itemBinding.tvIsNew.visible()
                    itemBinding.tvIsNew.text = it.orderDateStatus
                }
            }
        }

    }

    interface OnOrderClicked : BaseAdapter.BaseAdapterClickers<OrdersModel?> {
        fun onOrderClicked(model: OrdersModel)
    }
}