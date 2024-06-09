package com.wesal.askhail.features.payPackage.payPackageStepSelectBank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.databinding.RowBankBinding
import com.wesal.entities.models.BanksModel

class BanksAdapter (private val list: MutableList<BanksModel?>,private val callBack: OnBankClicked?)
    : RecyclerView.Adapter<BanksAdapter.HolderView>() {

    //: BaseAdapter<BanksModel?, BanksAdapter.OnBankClicked>(list, callBack) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<BanksModel>?) {
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
    inner class HolderView(private val itemBinding: RowBankBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: BanksModel?, position: Int) {
            model?.let {
                itemBinding.ivBankImage?.loadServerImage(it.bankAccountLogo)
                itemView.setOnClickListener { _->callBack?.onBankClicked(it) }
            }
        }

    }



    interface OnBankClicked : BaseAdapter.BaseAdapterClickers<BanksModel?> {
        fun onBankClicked(model: BanksModel)
    }
}
