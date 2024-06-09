package com.wesal.askhail.features.advertDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowAdvertSliderBinding
import com.wesal.entities.models.AdvMediaModel

class AdvertSliderAdapter(private val list: MutableList<AdvMediaModel?>,private val callBack: OnImageSliderClicked?)
    : RecyclerView.Adapter<AdvertSliderAdapter.HolderView>() {
    //: BaseAdapter<AdvMediaModel?, AdvertSliderAdapter.OnImageSliderClicked>(list, callBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowAdvertSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<AdvMediaModel>?) {
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
    inner class HolderView(private val itemBinding: RowAdvertSliderBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvMediaModel?, position: Int) {
            model?.let {
                itemBinding.ivSliderImage?.loadServerImage(it.mediaImage)
                itemView.setOnClickListener {_-> callBack?.onImageSliderClicked(it) }
                if (it.mediaVideo.isNullOrEmpty()){
                    itemBinding.ivPlay.gone()
                }else{
                    itemBinding.ivPlay.visible()
                }
            }
        }

    }


    interface OnImageSliderClicked : BaseAdapter.BaseAdapterClickers<AdvMediaModel?> {
        fun onImageSliderClicked(model: AdvMediaModel)
    }
}
