package com.wesal.askhail.features.browsingType

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBarWithWhite
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.databinding.ActivityBrowsingTypeBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.login.LoginActivity
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.typePhone.TypePhoneActivity
import com.wesal.domain.useCases.UseCaseImpl

class BrowsingTypeActivity : BaseActivity() {
    lateinit var binding: ActivityBrowsingTypeBinding;
    override fun layoutResource(): Int =R.layout.activity_browsing_type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBrowsingTypeBinding.inflate(layoutInflater)
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
        binding.viewLoginAsVisitor.setOnClickListener {
            UseCaseImpl().loginAsVisitor()
            sStartActivityWithClear<MainActivity>()
            UseCaseImpl().isInVisitorMode()
        }
    }
}