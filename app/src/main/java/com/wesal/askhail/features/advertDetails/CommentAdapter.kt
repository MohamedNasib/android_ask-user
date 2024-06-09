package com.wesal.askhail.features.advertDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowCommentBinding
import com.wesal.entities.models.CommentModel

class CommentAdapter(private val list: MutableList<CommentModel?>,private val myCallBack:OnCommentAction)

    : RecyclerView.Adapter<CommentAdapter.HolderView>() {
    //: BaseAdapter<CommentModel?,BaseAdapter.BaseAdapterClickers<CommentModel?>>(list, null) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<CommentModel>?) {
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
    inner class HolderView(private val itemBinding: RowCommentBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: CommentModel?, position: Int) {
            model?.let {
                itemBinding.tvCommenterName.text = "${it.commentVoterName} ${itemView.context.getString(R.string.says)}"
                itemBinding.tvCommentDate.text = it.commentTextCustomDate
                itemBinding.tvComment.text = it.commentText
                if (it.commentIfAdvertiserReplyYet){

                    itemBinding.viewReplay.visible()
                    itemBinding.tvReplay.visible()
                    itemBinding.tvReplayDate.text =it.replayDate
                    itemBinding.tvReplay.text = it.replay
                }else{
                    itemBinding.viewReplay.gone()
                    itemBinding.tvReplay.gone()
                }
                if (currentUserId==ownerUserId &&!it.commentIfAdvertiserReplyYet){
                    itemBinding.ivReplay.visible()
                }else{
                    itemBinding.ivReplay.gone()
                }
                itemBinding.ivReplay.setOnClickListener { _->myCallBack.onCommentReplay(it) }
                if (it.commentVoterId == currentUserId){
                    itemBinding.tvDeleteComment.visible()
                    itemBinding.tvDeleteComment.setOnClickListener {_-> myCallBack.onDeleteMyComment(it,false) }
                }else{
                    itemBinding.tvDeleteComment.gone()
                }


            }
        }

    }



    var currentUserId = -1
    var ownerUserId = -1


    fun removeComment(commentId: Int) {
        list.removeAll {
            it?.commentId ==commentId
        }
        notifyDataSetChanged()
    }

    interface OnCommentAction{
        fun onDeleteMyComment(commentModel: CommentModel,boolean: Boolean)
        fun onCommentReplay(commentModel: CommentModel)
    }
}
