package com.wesal.askhail.features.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowSpeicalAdvertBinding
import com.wesal.askhail.databinding.RowSpeicalAdvertMoreBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.SpecialAdvertisementModel

class HomeSpecialAdvertAdapter(
        private val list: MutableList<SpecialAdvertisementModel?>,
        private val callback:OnSpecialAdvert
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //: RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
         when(viewType){
            TYPE_ADVERT->{
                val itemBinding = RowSpeicalAdvertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SpecialAdvertVh(itemBinding)
            }
            else ->{
                val itemBinding = RowSpeicalAdvertMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SpecialAdvertMoreVh(itemBinding)
            }
        }
    }
    fun addMoreInList(listItems: List<SpecialAdvertisementModel>?) {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? SpecialAdvertVh)?.bind(list[position])
        (holder as? SpecialAdvertMoreVh)?.bind()
    }
    val currentUserId = UseCaseImpl().getCurrentUserId()

    companion object {
        const val TYPE_ADVERT = 1
        const val TYPE_MORE = 2
    }
    override fun getItemViewType(position: Int): Int {
        val model = list[position]
        return if (model==null){
            TYPE_MORE
        }else{
            TYPE_ADVERT
        }

    }



    inner class SpecialAdvertVh(private val itemBinding: RowSpeicalAdvertBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: SpecialAdvertisementModel?) {
            model?.let {
                itemBinding.ivImage?.loadServerImage(it.advPromotionalImage)
                itemBinding.tvTitle?.text = it.advTitle
                itemBinding.tvPrice?.text ="${it.advPrice} ${itemView.context.getString(R.string.sar)}"
                if (it.advPrice=="0"){
                    itemBinding.tvPrice.gone()
                }else{
                    itemBinding.tvPrice.visible()
                }
                if (it.advIsFavorite){
//                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_on)
                }else{
//                    itemBinding.ivIsFav.setImageResource(R.drawable.ic_fav_off)
                }
                if (currentUserId == it.advAdvertiserId){
                    itemBinding.ivIsFav.gone()
                }else{
                    itemBinding.ivIsFav.visible()
                }
                itemBinding.tvDistance?.text = "${it.advDistance} ${itemView.context.getString(R.string.km)}"
                itemBinding.tvViews?.text = it.advViews
                itemView.setOnClickListener {_-> callback.onViewAdvertDetails(it) }
                itemBinding.ivIsFav?.setOnClickListener {_->
                    callback.onToggleFavorite(it)
                    it.advIsFavorite = !it.advIsFavorite
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
    inner class SpecialAdvertMoreVh(private val itemBinding: RowSpeicalAdvertMoreBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
        fun bind() {
            itemView.setOnClickListener { callback.onSeeAllSpecialAdverts() }
        }

    }




    interface OnSpecialAdvert{
        fun onToggleFavorite(model:SpecialAdvertisementModel)
        fun onViewAdvertDetails(model:SpecialAdvertisementModel)
        fun onSeeAllSpecialAdverts()
    }

}
