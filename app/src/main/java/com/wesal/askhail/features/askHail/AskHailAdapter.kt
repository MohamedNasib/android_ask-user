package com.wesal.askhail.features.askHail

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
import com.wesal.askhail.databinding.RowAskhailBinding
import com.wesal.entities.models.AskHailModel

class AskHailAdapter(private val list: MutableList<AskHailModel?>, private val callBack: OnAskHail?)
    : RecyclerView.Adapter<AskHailAdapter.HolderView>() {
    //:    BaseAdapter<AskHailModel?, AskHailAdapter.OnAskHail>(list, callBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowAskhailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<AskHailModel>?) {
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
    inner class HolderView(private val itemBinding: RowAskhailBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AskHailModel?, position: Int) {
            model?.let {
                if (it.questionImage.isNullOrEmpty()){
//                    itemBinding.ivAskImage.gone()
                }else{
//                    itemBinding.ivAskImage.visible()
                    itemBinding.ivAskImage.loadServerImage(it.questionImage)
                }
                itemBinding.tvAskerName?.text = "${it.questionAdvertiserName} ${itemView.context.getString(R.string.asks)}"
                itemBinding.tvAskDate?.text = it.questionCustomDate
                itemBinding.tvAskTitle?.text = it.questionTitle
                itemBinding.tvAskReplay?.text = it.questionRepliesText
                if (it.questionDateStatus.isNullOrEmpty()){
                    itemBinding.tvIsNew.invisible()
                }else{
                    itemBinding.tvIsNew.visible()
                    itemBinding.tvIsNew.text = it.questionDateStatus
                }
                itemView.setOnClickListener { _->callBack?.onAskHail(it) }
                if (it.questionShowNameStatus == "active"){
                    itemBinding.tvAskerName.visible()
                }else{
                    itemBinding.tvAskerName.invisible()

                }
            }
        }

    }
    interface OnAskHail : BaseAdapter.BaseAdapterClickers<AskHailModel?> {
        fun onAskHail(model: AskHailModel)
    }
}
