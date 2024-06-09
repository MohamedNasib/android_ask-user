package com.wesal.askhail.features.prayAndWeather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.base.BaseViewHolder
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.loadServerImageForWeather
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowAdvertSliderBinding
import com.wesal.askhail.databinding.RowWeatherBinding
import com.wesal.askhail.features.advertDetails.AdvertSliderAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.DailyModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(private val list: MutableList<DailyModel?>,private val callBack: OnBusinessSection?)
    : RecyclerView.Adapter<WeatherAdapter.HolderView>() {

    //: BaseAdapter<DailyModel?, WeatherAdapter.OnBusinessSection>(list, callBack) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding = RowWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }
    fun addMoreInList(listItems: List<DailyModel>?) {
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
    inner class HolderView(private val itemBinding: RowWeatherBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: DailyModel?, position: Int) {
            model?.let {
                itemBinding.tvTemp.text = "${it.temp.day.toInt()}"
                itemBinding.tvDay.text = getWeekNameFromTime(it.dt)
                if (!it.weather.isNullOrEmpty()){
                    itemBinding.ivTemp.loadServerImageForWeather(it.weather[0].icon)
                }
            }
        }

        private fun getWeekNameFromTime(dt: Long): CharSequence? {

            val dateFormat = SimpleDateFormat("EEEE", Locale(UseCaseImpl().getSystemLanguage()))
            val dateTime = dateFormat.format(Date(dt*1000))
            return dateTime
        }
    }


    interface OnBusinessSection : BaseAdapter.BaseAdapterClickers<DailyModel?> {
        fun onBusinessSection(model: DailyModel)
    }
}