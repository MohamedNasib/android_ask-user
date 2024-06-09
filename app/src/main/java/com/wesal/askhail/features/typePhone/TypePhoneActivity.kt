package com.wesal.askhail.features.typePhone

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ActivityTypePhoneBinding
import com.wesal.askhail.features.confimPhone.ConfirmPhoneActivity
import com.wesal.domain.useCases.UseCaseImpl

class TypePhoneActivity : BaseActivity() {
    lateinit var binding: ActivityTypePhoneBinding
    private lateinit var from: NavPhonePage
    override fun layoutResource(): Int =R.layout.activity_type_phone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypePhoneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        from = intent.getParcelableExtra(ExtraConst.EXTRA_FROM)!!
        setToolbar()
        setUpView()
        clickers()
    }

    private fun setUpView() {
        when(from){
            NavPhonePage.FORGET_PASSWORD -> {
                binding.tvTitle.text = getString(R.string.reset_password)
                binding.tvSubTitle.text = getString(R.string.type_your_phone)
            }
            NavPhonePage.REGISTER ->{
                binding.tvTitle.text = getString(R.string.create_new_account)
                binding.tvSubTitle.text = getString(R.string.type_your_phone)
            }
            NavPhonePage.CHANGE_PHONE ->{

            }
        }

    }

    private fun clickers() {
        binding.btnNext.setOnClickListener {

            if (validateRequest()){
                when(from){
                    NavPhonePage.FORGET_PASSWORD ->{
                        makeRequestForForgetPassword()

                    }
                    NavPhonePage.REGISTER -> {
                        makeRequestForRegister()
                    }
                    NavPhonePage.CHANGE_PHONE -> {}
                }
            }
        }
    }

    private fun makeRequestForForgetPassword() {

        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().checkPhoneForForgetPassword(binding.tiePhone.value())}
        ){
            it?.let {
                sStartActivity<ConfirmPhoneActivity>(
                    ExtraConst.EXTRA_ADVERTISER_ID  to it.advertiserId,
                    ExtraConst.EXTRA_FROM to NavPhonePage.FORGET_PASSWORD
                )
            }
        }

    }

    private fun makeRequestForRegister() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().checkPhone(binding.tiePhone.value())}
        ){
            it?.let {
                sStartActivity<ConfirmPhoneActivity>(
                    ExtraConst.EXTRA_ADVERTISER_ID  to it.advertiserId,
                    ExtraConst.EXTRA_FROM to NavPhonePage.REGISTER
                )
            }
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validatePhone(binding.tilPhone, binding.tiePhone)) {
            binding.tiePhone.requestFocus();isValid = false
        }
        return isValid;
    }
}