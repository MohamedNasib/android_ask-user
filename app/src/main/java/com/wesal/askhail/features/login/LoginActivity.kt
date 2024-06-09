package com.wesal.askhail.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityLoginBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.askhail.features.typePhone.TypePhoneActivity
import com.wesal.domain.useCases.UseCaseImpl

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun layoutResource(): Int =R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        clickers()
    }

    private fun clickers() {
        binding.tvForgetPassword.setOnClickListener {
            sStartActivity<TypePhoneActivity>(
                ExtraConst.EXTRA_FROM to NavPhonePage.FORGET_PASSWORD
            )
        }
        binding.btnLogin.setOnClickListener {
            if (validateRequest()){
                performLogin()
            }
        }
    }

    private fun performLogin() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().login(binding.tiePhone.value(),binding.tiePassword.value())}
        ){
            sStartActivityWithClear<SplashActivity>()
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true

        if (!ValidationLayer.validatePhone(binding.tilPhone, binding.tiePhone)) {
            binding.tiePhone.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateLength(binding.tilPassword, binding.tiePassword)) {
            binding.tiePassword.requestFocus();isValid = false
        }
        return isValid
    }
}