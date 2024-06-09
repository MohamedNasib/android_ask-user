package com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.databinding.RowAttrSelectBinding
import com.wesal.askhail.databinding.RowAttrTextBinding
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.entities.models.AdvertFeaturesModel
import com.wesal.entities.models.AdvertSpecificationModel
import com.wesal.entities.models.FeatureDataModel
import timber.log.Timber

class SelectAttributeAdapter(
    private val activity: Activity,
    val list: List<AdvertFeaturesModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SELECT -> {
                val itemBinding =
                    RowAttrSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AttrSelectVh(itemBinding)

            }
            else -> {
                val itemBinding =
                    RowAttrTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AttrTextVh(itemBinding)
            }
        }
    }

    companion object {
        const val TYPE_SELECT = 1;
        const val TYPE_TEXT = -1;
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].featureType == "choose")
            return TYPE_SELECT
        else
            return TYPE_TEXT
    }


    override fun getItemCount(): Int = list.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AttrTextVh)?.bind(list[position], position)
        (holder as? AttrSelectVh)?.bind(list[position], position)
    }

    fun isValid(): Boolean {
        list.forEach {
//            if (it.attributeRequired=="required"){
            if (true) {
                if (it.selectedText.isNullOrEmpty()) {
                    activity.toasting("${it.featureName}  ${activity.getString(R.string.required_field)}")
                    return false
                }
            }
        }
        return true
    }

    fun matchValueForEditing(editingFeatures: List<AdvertSpecificationModel>) {
        for (x in editingFeatures) {
            list.firstOrNull { u -> u.featureId == x.specificationSectionFeature.featureId }.apply {
                if (this != null) {
                    if (this.featureType == "choose") {
                        Timber.e("MATCHING WITH SELECT")
                        this.selectedText = x.specificationAnswer
                        this.selectedValue = x.specificationAnswerId.toString()
                    } else {
                        Timber.e("MATCHING WITH TEXT")
                        this.selectedText = x.specificationAnswer
                        this.selectedValue = x.specificationAnswer
                    }
                }
            }
        }
        notifyDataSetChanged()

    }

    inner class AttrSelectVh(private val itemBinding: RowAttrSelectBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvertFeaturesModel, position: Int) {
            itemBinding.tilAdvertAttrSelect.hint = model.featureName
            if (model.selectedText.isNullOrEmpty()) {
                itemBinding.tilAdvertAttrSelect.hint = model.featureName
                itemBinding.tieAdvertAttrSelect.setText(null)

            } else {
                itemBinding.tieAdvertAttrSelect.setText(model.selectedText)
            }
            itemBinding.tieAdvertAttrSelect.setOnClickListener {
                SingleChoiceDialog(
                    activity,
                    model.featureName,
                    model.featureData,
                    object : OnSingleDialogSelected<FeatureDataModel> {
                        override fun onSelected(value: FeatureDataModel) {
                            model.apply {
                                this.selectedText = value.dataTitle
                                this.selectedValue = value.dataId.toString()
                                notifyDataSetChanged()
                            }
                        }

                    }
                )
            }

        }
    }

    inner class AttrTextVh(private val itemBinding: RowAttrTextBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AdvertFeaturesModel, position: Int) {
            itemBinding.tilAdvertAttrTyping.hint = model.featureName
            if (model.selectedText.isNullOrEmpty()) {
                itemBinding.tieAdvertAttrTyping.setText("")
            } else {
                itemBinding.tieAdvertAttrTyping.setText(model.selectedText)
            }
            itemBinding.tieAdvertAttrTyping.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    model.apply {
                        this.selectedText = itemBinding.tieAdvertAttrTyping.value()
                        this.selectedValue = itemBinding.tieAdvertAttrTyping.value()
                    }
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
            itemBinding.tieAdvertAttrTyping.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
//                    Log.e("hasFocus"," "+ itemBinding.etTextValue.value())
                } else {
//                    Log.e("X hasFocus"," "+ itemBinding.etTextValue.value())
                    model.apply {
                        this.selectedText = itemBinding.tieAdvertAttrTyping.value()
                        this.selectedValue = itemBinding.tieAdvertAttrTyping.value()
                    }
                }
            }
        }
    }
}
