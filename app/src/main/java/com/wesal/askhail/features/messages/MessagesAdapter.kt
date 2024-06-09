package com.wesal.askhail.features.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowMessageBinding
import com.wesal.entities.models.MessagesModel

class MessagesAdapter(private val list: MutableList<MessagesModel?>,private val callBack: OnMessageClicked?) :
    RecyclerView.Adapter<MessagesAdapter.HolderView>() {
    //BaseAdapter<MessagesModel?, MessagesAdapter.OnMessageClicked>(list, callBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<MessagesModel>?) {
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
    inner class HolderView(private val itemBinding: RowMessageBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: MessagesModel?, position: Int) {
            model?.let {
                if (it.chatImage.isNullOrEmpty()){
//                    itemBinding.ivMessageImage.gone()
                }else{
//                    itemBinding.ivMessageImage.visible()
                    itemBinding.ivMessageImage.loadServerImage(it.chatImage)
                }
                itemBinding.tvMessageTitle.text = it.chatTitle
                itemBinding.tvMessageSubTitle.text = it.chatLastMessage
                itemBinding.tvMessageDate.text = it.chatLastMessageDate
                if (it.chatUnreadMessagesCount==0){
                    itemBinding.tvUnReadCount.gone()
                }else{
                    itemBinding.tvUnReadCount.visible()
                    itemBinding.tvUnReadCount.text ="${it.chatUnreadMessagesCount}"
                }
                itemView.setOnClickListener { _->
                    callBack?.onMessage(it)
                    it.chatUnreadMessagesCount=0;
                    notifyItemChanged(adapterPosition)
                }
            }
        }

    }
    interface OnMessageClicked : BaseAdapter.BaseAdapterClickers<MessagesModel?> {
        fun onMessage(model: MessagesModel)
    }
}