package com.wesal.askhail.features.myInfo

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityMyInfoBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.changePassword.ChangePasswordActivity
import com.wesal.askhail.features.editInfo.EditInfoActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AccountInformationModel
import timber.log.Timber

class MyInfoActivity : BaseActivity() {
    lateinit var binding: ActivityMyInfoBinding
    override fun layoutResource(): Int = R.layout.activity_my_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.acc_myinfo))
    }

    override fun onResume() {
        super.onResume()
        launching()
    }

    private fun launching() {

         ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getUserInfo() }
        ) {
            if (it != null) {
                updateUi(it)
            }else{
                Timber.e("NULLLLLLLLLLLLLL")
            }
        }
    }

    private fun updateUi(q: AccountInformationModel) {

        binding.tvName.setText(q.advertiserName)
        binding.tvEmail.setText(q.advertiserEmail)
        binding.tvPhone.setText(q.advertiserMobile)

        binding.tvEditInfo.setOnClickListener {
            sStartActivity<EditInfoActivity>(
                ExtraConst.EXTRA_MODEL to q
            )
        }
        binding.tvChangePassword.setOnClickListener {
            sStartActivity<ChangePasswordActivity>()
        }
    }
}