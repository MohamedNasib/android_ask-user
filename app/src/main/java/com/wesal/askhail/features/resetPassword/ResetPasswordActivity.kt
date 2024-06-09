package com.wesal.askhail.features.resetPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivityResetPasswordBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.useCases.UseCaseImpl
class ResetPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    private var advertiserId: Int =-1

    override fun layoutResource(): Int =R.layout.activity_reset_password
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar()
        advertiserId = intent.getIntExtra(ExtraConst.EXTRA_ADVERTISER_ID,-1)
        clicker()
    }

    private fun clicker() {
        binding.btnConfirm.setOnClickListener {
            if (validateRequest()){
                performRequest()
            }
        }
    }

    private fun performRequest() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().resetPassword(advertiserId,binding.tiePassword.value(),binding.tieRePassword.value())}
        ){
            sStartActivityWithClear<SplashActivity>()
        }

    }

    private fun validateRequest(): Boolean {

        var isValid = true
        if (!ValidationLayer.validateLength(binding.tilPassword, binding.tiePassword)) {
            binding.tiePassword.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateMatch(binding.tieRePassword, binding.tiePassword,binding.tilRePassword)) {
            binding.tieRePassword.requestFocus();isValid = false
        }
        return isValid

    }
}