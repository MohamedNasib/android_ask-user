package com.wesal.askhail.features.workWithUs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.databinding.RowJobBinding
import com.wesal.entities.models.AdvertSpecificationModel
import com.wesal.entities.models.JobsModel

class JobsAdapter(private val list: MutableList<JobsModel?>, private val callBack: OnJob?) :
    RecyclerView.Adapter<JobsAdapter.HolderView>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }

    fun addMoreInList(listItems: List<JobsModel>?) {
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

    inner class HolderView(private val itemBinding: RowJobBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: JobsModel?, position: Int) {
            model?.let {
                itemBinding.tvJobName?.text = it.jobTitle
                itemBinding.tvJobDesc?.text = it.jobDescription
                itemView.setOnClickListener { _ -> callBack?.onJob(it) }
            }
        }

    }


    interface OnJob : BaseAdapter.BaseAdapterClickers<JobsModel?> {
        fun onJob(model: JobsModel)
    }
}