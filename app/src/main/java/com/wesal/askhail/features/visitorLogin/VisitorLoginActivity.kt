package com.wesal.askhail.features.visitorLogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBarWithWhite
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ActivityVisitorLoginBinding
import com.wesal.askhail.features.login.LoginActivity
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.typePhone.TypePhoneActivity
import com.wesal.domain.useCases.UseCaseImpl

class VisitorLoginActivity : BaseActivity() {
    lateinit var binding: ActivityVisitorLoginBinding;
    override fun layoutResource(): Int =R.layout.activity_visitor_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitorLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBarWithWhite()
        clickers()
    }
    private fun clickers() {
        binding.viewLoginExistAccount.setOnClickListener {
            sStartActivity<LoginActivity>()
        }
        binding.viewCreateAccount.setOnClickListener {
            sStartActivity<TypePhoneActivity>(
                ExtraConst.EXTRA_FROM to NavPhonePage.REGISTER
            )
        }
    }

}