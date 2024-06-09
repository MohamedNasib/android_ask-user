package com.wesal.askhail.features.highlightAdvert.highlightStepInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ConstantsObjects
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityConfirmPhoneBinding
import com.wesal.askhail.databinding.ActivityHighlightStepInfoBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.highlightAdvert.highlightStepPackages.HighlightStepPackagesActivity
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.HighlightInfoModel
import com.wesal.entities.models.KeyValueModel

class HighlightStepInfoActivity : BaseActivity() {
    lateinit var binding: ActivityHighlightStepInfoBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var advertId: Int = -1
    private var selectedPlace: KeyValueModel? = null
    override fun layoutResource(): Int = R.layout.activity_highlight_step_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighlightStepInfoBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1)
        setWhiteActivity()
        setToolbar(getString(R.string.highlight_advert))
        launching()
    }

    private fun launching() {

        ParaNormalProcess.viewProcessActivity(
            this@HighlightStepInfoActivity,
            { UseCaseImpl().getHighlightInfo() }
        ) {
            it?.let { it1 -> updateUi(it1) }
        }
    }

    private fun updateUi(it: HighlightInfoModel) {

        binding.ivHighlightImage.loadServerImage(it.specialImage)
        binding.tvDes.text = it.specialDescription
        binding.rvHighlight.layoutManager = LinearLayoutManager(this)
        val adapter = HighlightAdapter(it.specialAdvantages.toMutableList(), null)
        binding.rvHighlight.adapter = adapter

        binding.tieAdvertPlace.setOnClickListener {
            SingleChoiceDialog(
                this@HighlightStepInfoActivity,
                getString(R.string.advert_place),
                ConstantsObjects.getHighlightPlace(this@HighlightStepInfoActivity),
                object : OnSingleDialogSelected<KeyValueModel> {
                    override fun onSelected(value: KeyValueModel) {
                        selectedPlace = value
                        binding.tieAdvertPlace.setText(selectedPlace?.name)
                    }
                }
            )
        }
        binding.btnConfirmNext.setOnClickListener {
            if (selectedPlace==null){
                toasting(R.string.select_advert_place)
            }else{
                sStartActivity<HighlightStepPackagesActivity>(
                    ExtraConst.EXTRA_ADVERT_ID to advertId,
                    ExtraConst.EXTRA_ADVERT_PLACE to selectedPlace?.key
                )
            }
        }
    }
}