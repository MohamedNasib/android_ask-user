package com.wesal.askhail.features.prayAndWeather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.invisible
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowPrayBinding
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.CustomPrayingTimeModel

class PrayingTimeAdapter (private val list: MutableList<CustomPrayingTimeModel?>,private val callBack: OnBusinessSection?)
    : RecyclerView.Adapter<PrayingTimeAdapter.HolderView>() {

    //: BaseAdapter<CustomPrayingTimeModel?, PrayingTimeAdapter.OnBusinessSection>(list, callBack) {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowPrayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<CustomPrayingTimeModel>?) {
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
    inner class HolderView(private val itemBinding: RowPrayBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: CustomPrayingTimeModel?, position: Int) {
            model?.let {
                itemBinding.tvPrayName.text = it.name
                itemBinding.tvPrayTime.text = it.time
                if (adapterPosition == itemCount-1){
                    itemBinding.viewLine.invisible()
                }else{
                    itemBinding.viewLine.visible()
                }
            }
        }

    }


    interface OnBusinessSection : BaseAdapter.BaseAdapterClickers<CustomPrayingTimeModel?> {
        fun onBusinessSection(model: CustomPrayingTimeModel)
    }
}