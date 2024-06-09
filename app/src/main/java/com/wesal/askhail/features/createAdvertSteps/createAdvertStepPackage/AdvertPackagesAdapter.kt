package com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowAdvertPackageBinding
import com.wesal.entities.models.PackageModel

class AdvertPackagesAdapter(private val list: MutableList<PackageModel?>, private val callBack: OnPackageClicked?,
                            private val isHighlight:Boolean=false)
    : RecyclerView.Adapter<AdvertPackagesAdapter.HolderView>() {
    //: BaseAdapter<PackageModel?, AdvertPackagesAdapter.OnPackageClicked>(list, callBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowAdvertPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<PackageModel>?) {
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
    inner class HolderView(private val itemBinding: RowAdvertPackageBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: PackageModel?, position: Int) {
            model?.let {
                itemBinding.tvPackageName?.text = it.packageName
                itemBinding.tvPackageDesc.text = it.packageDescription
                itemBinding.tvPackageCount.text = it.packageAdvertisementsCount
                if (it.packagePrice=="0"){
                    itemBinding.tvPackagePrice.text = itemView.context.getString(R.string.forfree)

                }else{
                    itemBinding.tvPackagePrice.text = it.packagePrice

                }
                itemBinding.tvPackagePeriod.text = it.packageDurationInDays
                itemView.setOnClickListener { _->callBack?.onPackageClicked(it) }

                if (isHighlight){
                    itemBinding.viewAdvertsCount.gone()
                    itemBinding.viewAdvertsCountLine.gone()
                }else{
                    itemBinding.viewAdvertsCount.visible()
                    itemBinding.viewAdvertsCountLine.visible()
                }
            }
        }
    }

    interface OnPackageClicked : BaseAdapter.BaseAdapterClickers<PackageModel?> {
        fun onPackageClicked(model: PackageModel)
    }
}
