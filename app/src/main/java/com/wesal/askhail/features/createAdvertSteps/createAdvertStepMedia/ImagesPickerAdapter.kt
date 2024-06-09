package com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.core.base.BaseAdapter
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.databinding.RowImagesPickersBinding
import com.wesal.entities.models.AdvMediaModel
import java.io.File

class ImagesPickerAdapter(
    val list: MutableList<AdvMediaModel?>,
    val callBack: OnImagesClicked
) : RecyclerView.Adapter<ImagesPickerAdapter.HolderView>() {

    //: BaseAdapter<AdvMediaModel, ImagesPickerAdapter.OnImagesClicked>(list, callBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderView {
        val itemBinding =
            RowImagesPickersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderView(itemBinding)
    }

    fun addMoreInList(listItems: List<AdvMediaModel>?) {
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

    @SuppressLint("NotifyDataSetChanged")
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

    inner class HolderView(private val itemBinding: RowImagesPickersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvMediaModel?, position: Int) {

            model?.let {
                itemBinding.ivClose?.setOnClickListener { _ ->
                    list.removeAt(position)
                    notifyDataSetChanged()
                    callBack?.onImagesClicked(it)
                }

                if (model.mediaImage.startsWith("http")) {
                    itemBinding.ivMedia?.loadServerImage(model.mediaImage)
                } else {
                    itemBinding.ivMedia?.loadServerImage(File(model.mediaImage))
                }
                if (model.mediaVideo.isNullOrEmpty()) {
                    itemBinding.tvIsVideo.gone()
                } else {
                    itemBinding.tvIsVideo.setText(convertIntToSeconds(it.videoDuration))
                    itemBinding.tvIsVideo.visible()
                }
            }
        }

        private fun convertIntToSeconds(videoDuration: Int): String {

            return DateUtils.formatElapsedTime(videoDuration.toLong())
        }
    }

    fun canAddMoreVideos(): Boolean {
        val find = list.filter {
            !it?.mediaVideo.isNullOrEmpty()
        }
        return find.size != 2

    }

    interface OnImagesClicked : BaseAdapter.BaseAdapterClickers<AdvMediaModel> {
        fun onImagesClicked(model: AdvMediaModel)
    }

}
