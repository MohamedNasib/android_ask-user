package com.wesal.askhail.subFeatures.dialogSingle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.databinding.RowSingleChoiseBinding


class SingleChoiceAdapter(
    private val adapterList: List<String>,
    private val onSingleClicked: OnSingleClicked
) :
    RecyclerView.Adapter<SingleChoiceAdapter.HolderView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowSingleChoiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }

    override fun getItemCount(): Int = adapterList.size
    override fun onBindViewHolder(holder: HolderView, position: Int) {
        holder.bind(adapterList[position],position)
    }
    inner class HolderView(private val itemBinding: RowSingleChoiseBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(s: String, position: Int) {
            itemBinding.text1.text = s
            itemView.setOnClickListener {
                onSingleClicked.onClicked(position)
            }

        }

    }

    interface OnSingleClicked{
        fun onClicked(position:Int)
    }
}
