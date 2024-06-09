package com.wesal.askhail.features.chatActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.databinding.RowChatMeBinding
import com.wesal.askhail.databinding.RowChatOtherBinding
import com.wesal.askhail.databinding.RowLoadingBinding
import com.wesal.entities.models.MessagesDataModel
import timber.log.Timber

class ChatAdapter(
    private val list: MutableList<MessagesDataModel?>
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //: RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_MY_CHAT -> {
                val itemBinding = RowChatMeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyChatVh(itemBinding)
            }
            VIEW_OTHER_CHAT -> {
                val itemBinding = RowChatOtherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OtherChatVh(itemBinding)
            }
            else -> {
                val itemBinding = RowLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HolderView(itemBinding)

            }

        }

    }

    fun addMoreInList(listItems: List<MessagesDataModel>?) {
        Timber.e("XXXXXXXXXXX ==> ${listItems?.size}")
        listItems?.let {
            val size = this.list.size
            list.addAll(it)
            val sizeNew = this.list.size
            notifyItemRangeChanged(size, sizeNew)
        }
    }
    fun addMoreInListWithClearOther(listItems: List<MessagesDataModel>?) {
        listItems?.let {
            this.list.clear()
            this.list.addAll(it)
            notifyDataSetChanged()
        }
    }
    fun removeLoadingFooter() {
        val position = list.size - 1
        list.removeAt(position)
        notifyItemRemoved(list.size)
    }

    override fun getItemCount(): Int = list.size
    fun addLoadingFooter() {
        list.add(null)
        notifyItemInserted(list.size - 1)
    }

    fun removeFromList(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyChatVh) {
            holder.bind(list[position])
        }
        if (holder is OtherChatVh) {
            holder.bind(list[position])
        }

    }
    inner class HolderView(private val itemBinding: RowLoadingBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

    }



    var currentUserId = -1
    companion object {
        private const val VIEW_PROG = 0
        private const val VIEW_OTHER_CHAT = 1
        private const val VIEW_MY_CHAT = 2
    }

    override fun getItemViewType(position: Int): Int {
        val messagesDataModel = list[position]
        return if (messagesDataModel == null) {
            return VIEW_PROG
        } else {
            if (messagesDataModel.messageSenderId == currentUserId) {
                return VIEW_MY_CHAT
            } else {
                VIEW_OTHER_CHAT
            }
        }
    }

    fun addOneMsg(model: MessagesDataModel) {
        list.add(0, model)
        notifyItemInserted(0)
    }
    inner class MyChatVh(private val itemBinding: RowChatMeBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: MessagesDataModel?) {
            model?.let {
                itemBinding.tvMyMessage.text = it.messageText
                itemBinding.tvMyDate.text = it.messageCustomDate
            }
        }
    }

    inner class OtherChatVh(private val itemBinding: RowChatOtherBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: MessagesDataModel?) {
            model?.let {
                itemBinding.tvOtherMessage.text = it.messageText
                itemBinding.tvOtherDate.text = it.messageCustomDate
            }

        }
    }
}
