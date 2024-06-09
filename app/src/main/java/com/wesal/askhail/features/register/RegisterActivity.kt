package com.wesal.askhail.features.register

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivityRegisterBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.more.fixedPageDetails.FixedPageDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl

 class RegisterActivity : BaseActivity() {
     lateinit var binding: ActivityRegisterBinding
    private var advertiserId: Int = -1
    override fun layoutResource(): Int = R.layout.activity_register
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertiserId = intent.getIntExtra(ExtraConst.EXTRA_ADVERTISER_ID, -1)
        setToolbar()
        clicker()
    }

    private fun clicker() {
        binding.btnConfirm.setOnClickListener {
            if (validateRequest()) {
                performRequest()
            }
        }
        binding.tvTermsAndConditions.setOnClickListener {
            sStartActivity<FixedPageDetailsActivity>(
                ExtraConst.EXTRA_FIXED_PAGE_SLUG to "Terms-and-Conditions",
                ExtraConst.EXTRA_FIXED_PAGE_TITLE to getString(R.string.terms_and_condition)
            )
        }
    }

    private fun performRequest() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().register(advertiserId,binding.tieFullName.value(),binding.tieEmail.value(),binding.tiePassword.value())}
        ){
            sStartActivityWithClear<SuccessPageActivity>(
                ExtraConst.EXTRA_SUCCESS to SuccessRoute.REGISTER
            )
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

        if (!ValidationLayer.validateLength(binding.tilPassword, binding.tiePassword)) {
            binding.tiePassword.requestFocus();isValid = false
        }
        if (!binding.cbTerms.isChecked) {
            toasting(R.string.must_accept_terms)
            return false
        }
    return isValid
}
}