package com.wesal.askhail.features.advertDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowRateBinding
import com.wesal.entities.models.RateModel

class RateAdapter(private val list: MutableList<RateModel?>, private val myCallBack:OnRateAction)
    : RecyclerView.Adapter<RateAdapter.HolderView>() {
    //:        BaseAdapter<RateModel?, BaseAdapter.BaseAdapterClickers<RateModel?>>(list, null) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<RateModel>?) {
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
    inner class HolderView(private val itemBinding: RowRateBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: RateModel?, position: Int) {
            model?.let {
                itemBinding.tvRaterName.text = "${it.rateVoterName} ${itemView.context.getString(R.string.rated)}"
                itemBinding.tvRateDate.text = it.rateCustomDate
                itemBinding.srbService.rating = it.rate
                if (currentUserId == it.rateVoterId){
                    itemBinding.tvDeleteRate.visible()
                    itemBinding.tvDeleteRate.setOnClickListener { _-> myCallBack.onDeleteMyRate(it) }
                }else{
                    itemBinding.tvDeleteRate.gone()
                }
            }
        }
    }
    var currentUserId = -1
    fun removeRate(rateId: Int) {
        list.removeAll {
            it?.rateId ==rateId
        }
        notifyDataSetChanged()
    }
    interface OnRateAction{
        fun onDeleteMyRate(RateModel: RateModel)
    }
}
