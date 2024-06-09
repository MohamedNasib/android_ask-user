package com.wesal.askhail.features.subSectionsItems.advertsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowAdvertBinding
import com.wesal.askhail.features.advertDetails.AdvertSliderAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.AdvertSpecificationModel

class AdvertsListAdapter(
    private val lists: MutableList<AdvertModel?>,
    private val isBusiness: Boolean,
    private val callBacks: OnAdvertCallBack?
)

    : RecyclerView.Adapter<AdvertsListAdapter.HolderView>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowAdvertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.bind(lists[position], position)
    }

    inner class HolderView(private val itemBinding: RowAdvertBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvertModel?, position: Int) {
            model?.let {
                /**
                 * init the Pager
                 */
                itemBinding.vpAdvertSlider.adapter = AdvertSliderAdapter(
                    it.advMedia.toMutableList(),
                    object : AdvertSliderAdapter.OnImageSliderClicked {
                        override fun onImageSliderClicked(model: AdvMediaModel) {
                            callBacks?.onViewAdvertDetails(it)
                        }
                    }
                )
                itemBinding.indicatorAdvert.apply {
                    setViewPager(itemBinding.vpAdvertSlider)
                }

                itemBinding.ivImage?.loadServerImage(it.advPromotionalImage)
                itemBinding.tvTitle?.text = it.advTitle
                itemBinding.tvPrice?.text =
                    "${it.advPrice} ${itemView.context.getString(R.string.sar)}"
                if (it.advPrice == "0") {
//                    itemBinding.tvPrice.gone()
                } else {
//                    itemBinding.tvPrice.visible()
                }
                if (it.advIsFavorite) {
                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_on)
                } else {
                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_off)
                }
                if (currentUserId == it.advAdvertiserId) {
                    itemBinding.ivIsFav.gone()
                } else {
                    itemBinding.ivIsFav.visible()
                }
                itemBinding.tvDistance?.text =
                    "${it.advDistance} ${itemView.context.getString(R.string.km)}"
                itemBinding.tvViews?.text = it.advViews
                if (isBusiness) {
//                    itemBinding.viewBusinessSection?.visible()
                    itemBinding.tvRates?.text = it.advTotalRate
                    itemBinding.tvAvailable?.text = it.advAvailableCustomStatus
                    if (it.advAvailableStatus == "unavailable") {
                        itemBinding.tvAvailable?.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorInputError
                            )
                        )
                    } else {
                        itemBinding.tvAvailable?.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.textGreen
                            )
                        )
                    }
                } else {
//                    itemBinding.viewBusinessSection?.gone()
                }
                if (it.advSpecialStatus.isNotEmpty()) {
                    itemBinding.tvSpecial?.visible()
                    itemBinding.tvSpecial?.text = it.advSpecialStatus
                } else {
                    itemBinding.tvSpecial?.gone()
                }
                itemView.setOnClickListener { _ -> callBacks?.onViewAdvertDetails(it) }
                itemBinding.ivIsFav?.setOnClickListener { _ ->
                    callBacks?.onToggleFavorite(it)
                    it.advIsFavorite = !it.advIsFavorite
                    notifyItemChanged(adapterPosition)
                }
            }
        }

    }
    //:BaseAdapter<AdvertModel?, AdvertsListAdapter.OnAdvertCallBack>(lists, callBacks) {
    val currentUserId = UseCaseImpl().getCurrentUserId()
    interface OnAdvertCallBack  {
            fun onViewAdvertDetails(model:AdvertModel)
            fun onToggleFavorite(model:AdvertModel)
    }
}
