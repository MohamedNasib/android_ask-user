package com.wesal.askhail.features.inactiveAdvertList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowHomeRealstateBinding
import com.wesal.askhail.databinding.RowInactiveAdvertBinding
import com.wesal.askhail.features.home.HomeRealStateAdapter
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.SectionModel
class InActiveAdvertsAdapter(
    private val lists: MutableList<AdvertModel?>,
    private val isBusiness: Boolean,
    private val callBacks: OnInActiveAdvertCallBack?
)
    : RecyclerView.Adapter<InActiveAdvertsAdapter.HolderView>() {
    //: BaseAdapter<AdvertModel?, InActiveAdvertsAdapter.OnInActiveAdvertCallBack>(lists, callBacks) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowInactiveAdvertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<AdvertModel>?) {
        listItems?.let {
            val size = this.lists.size
            lists.addAll(it)
            val sizeNew = this.lists.size
            notifyItemRangeChanged(size, sizeNew)
        }
    }
    override fun getItemCount(): Int = lists.size
    fun addLoadingFooter() {
        lists.add(null)
        notifyItemInserted(lists.size - 1)
    }

    fun removeLoadingFooter() {
        val position = lists.size - 1
        lists.removeAt(position)
        notifyItemRemoved(lists.size)
    }

    fun removeFromList(position: Int) {
        lists.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearData() {
        lists.clear()
    }

    override fun onBindViewHolder(holder: HolderView, position: Int) {
        holder.bind(lists[position],position)
    }
    inner class HolderView(private val itemBinding: RowInactiveAdvertBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: AdvertModel?, position: Int) {
            model?.let {
                itemBinding.ivImage.loadServerImage(it.advPromotionalImage)
                itemBinding.tvTitle.text = it.advTitle
                itemBinding.tvPrice.text =
                    "${it.advPrice} ${itemView.context.getString(R.string.sar)}"
                if (it.advIsFavorite) {
                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_on)
                } else {
                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_off)
                }
                itemBinding.ivIsFav.gone()
                itemBinding.tvDistance.text =
                    "${it.advDistance} ${itemView.context.getString(R.string.km)}"
                itemBinding.tvViews.text = it.advViews
                if (isBusiness) {
                    itemBinding.viewBusinessSection?.visible()
                    itemBinding.tvRates?.text = it.advTotalRate
                    itemBinding.tvAvailable?.text = it.advAvailableCustomStatus
                    if (it.advAvailableStatus =="unavailable"){
                        itemBinding.tvAvailable?.setTextColor(
                            ContextCompat.getColor(itemView.context,
                                R.color.colorInputError))
                    }else{
                        itemBinding.tvAvailable?.setTextColor(
                            ContextCompat.getColor(itemView.context,
                                R.color.textGreen))
                    }
                } else {
                    itemBinding.viewBusinessSection?.gone()
                }
                if (it.advSpecialStatus.isNotEmpty()){
                    itemBinding.tvSpecial?.visible()
                    itemBinding.tvSpecial?.text = it.advSpecialStatus
                }else{
                    itemBinding.tvSpecial?.gone()
                }
                itemView.setOnClickListener {_-> callBacks?.onInActiveClicked(it) }
            }
        }

    }

    interface OnInActiveAdvertCallBack : BaseAdapter.BaseAdapterClickers<AdvertModel?> {
        fun onInActiveClicked(model: AdvertModel)
    }
}
