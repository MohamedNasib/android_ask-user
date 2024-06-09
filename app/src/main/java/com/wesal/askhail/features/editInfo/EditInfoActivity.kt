package com.wesal.askhail.features.editInfo

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityEditInfoBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AccountInformationModel


class EditInfoActivity : BaseActivity() {
    lateinit var binding: ActivityEditInfoBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private lateinit var model: AccountInformationModel

    override fun layoutResource(): Int =R.layout.activity_edit_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.edit_data))
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        model = intent.getParcelableExtra(ExtraConst.EXTRA_MODEL)!!
        setUpView()
        clickers()
    }


    private fun setUpView() {
        binding.tieFullName.setText(model.advertiserName)
        binding.tieEmail.setText(model.advertiserEmail)
        binding.tiePhone.setText(model.advertiserMobile)
    }

    private fun clickers() {
        binding.btnSaveChanges.setOnClickListener {
            if (validateRequest()){
                makeRequest()
            }
        }

    }

    private fun makeRequest() {

        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().editPersonalInfo(
                binding.tieFullName.value(),
                binding.tieEmail.value(),
                binding.tiePhone.value()
            )}
        ){
            toasting(R.string.changes_saved,true)
            onBackPressed()
        }
    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilFullName, binding.tieFullName)) {
            binding.tieFullName.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateEmail(binding.tilEmail, binding.tieEmail)) {
            binding.tieEmail.requestFocus();isValid = false
        }
        if (!ValidationLayer.validatePhone(binding.tilPhone, binding.tiePhone)) {
            binding.tiePhone.requestFocus();isValid = false
        }
        return isValid

    }
}