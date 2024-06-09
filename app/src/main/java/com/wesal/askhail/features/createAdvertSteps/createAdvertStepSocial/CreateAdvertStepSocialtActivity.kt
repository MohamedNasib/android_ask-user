package com.wesal.askhail.features.createAdvertSteps.createAdvertStepSocial

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityCreateAdvertStepSocialtBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.advertDetails.EditSocialModel
import com.wesal.domain.useCases.UseCaseImpl

class CreateAdvertStepSocialtActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAdvertStepSocialtBinding;
    private lateinit var tool_binding: MyToolbarStepsBinding
    private var editModel: EditSocialModel?=null
    private var isEdit: Boolean = false
    private var advertId: Int = -1
    override fun layoutResource(): Int = R.layout.activity_create_advert_step_socialt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertStepSocialtBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1);
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        editModel = intent.getParcelableExtra(ExtraConst.EXTRA_MODEL)

        hideStatusBar()
        if (isEdit) {
            setToolbar(getString(R.string.edit_advert_social))
            tool_binding.tvSteps.text = " "
            binding.btnConfirmSocial.setText(R.string.save_changes)
            setUpViewForEdit()
        } else {
            setToolbar(getString(R.string.create_new_advert))
            tool_binding.tvSteps.text = "7/7"
        }
        clickers()
    }

    private fun clickers() {
        binding.btnConfirmSocial.setOnClickListener {
            if (validateRequest()) {
                if (isEdit) {
                    performActionForEdit()

                } else {
                    performActionForCreate()
                }
            }
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateWebSite(binding.tilTwitter, binding.tieTwitter)) {
            binding.tieTwitter.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateWebSite(binding.tilInsta, binding.tieInsta)) {
            binding.tieInsta.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateWebSite(binding.tilSnap, binding.tieSnap)) {
            binding.tieSnap.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateWebSite(binding.tilFace, binding.tieFace)) {
            binding.tieFace.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateWebSite(binding.tilWebSite, binding.tieWebSite)) {
            binding.tieWebSite.requestFocus();isValid = false
        }
        return isValid


    }

    private fun performActionForCreate() {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        binding.tieTwitter.value().let {
            if (it.isNotEmpty()) {
                map["twitter"] = it
            }
        }
        binding.tieInsta.value().let {
            if (it.isNotEmpty()) {
                map["instagram"] = it
            }
        }
        binding.tieSnap.value().let {
            if (it.isNotEmpty()) {
                map["snapchat"] = it
            }
        }
        binding.tieFace.value().let {
            if (it.isNotEmpty()) {
                map["facebook"] = it
            }
        }
        binding.tieWebSite.value().let {
            if (it.isNotEmpty()) {
                map["website"] = it
            }
        }
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createAdvertStepSocial(map) }
        ) {
            sStartActivity<SuccessPageActivity>(
                ExtraConst.EXTRA_SUCCESS to SuccessRoute.CREATE_ADVERT
            )
        }

    }
    private fun performActionForEdit() {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        binding.tieTwitter.value().let {
            if (it.isNotEmpty()) {
                map["twitter"] = it
            }
        }
        binding.tieInsta.value().let {
            if (it.isNotEmpty()) {
                map["instagram"] = it
            }
        }
        binding.tieSnap.value().let {
            if (it.isNotEmpty()) {
                map["snapchat"] = it
            }
        }
        binding.tieFace.value().let {
            if (it.isNotEmpty()) {
                map["facebook"] = it
            }
        }
        binding.tieWebSite.value().let {
            if (it.isNotEmpty()) {
                map["website"] = it
            }
        }
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().editAdvertStepSocial(map) }
        ) {
            toasting(R.string.changes_saved,true)
            onBackPressed()
        }

    }

    private fun setUpViewForEdit() {
        editModel?.let {
            binding.tieTwitter.setText(it.twitter)
            binding.tieInsta.setText(it.instagram)
            binding.tieSnap.setText(it.snapchat)
            binding.tieFace.setText(it.facebook)
            binding.tieWebSite.setText(it.website)
        }

    }
}