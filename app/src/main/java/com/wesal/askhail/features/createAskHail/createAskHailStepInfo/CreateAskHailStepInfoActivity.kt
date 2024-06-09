package com.wesal.askhail.features.createAskHail.createAskHailStepInfo

import android.os.Bundle
import android.text.Html
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAskHailStepInfoBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAskHail.createAskHailStepMedia.CreateAskHailStepMediaActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSection.CreateOrderStepSectionActivity
import com.wesal.domain.useCases.UseCaseImpl

class CreateAskHailStepInfoActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAskHailStepInfoBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    override fun layoutResource(): Int =R.layout.activity_create_ask_hail_step_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAskHailStepInfoBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()
        setToolbar(getString(R.string.create_new_ask))
        tool_binding.tvSteps.text = "1/3"
        launching()
        clickers()
    }
    private fun clickers() {
        binding.btnCont.setOnClickListener {
            sStartActivity<CreateAskHailStepMediaActivity>()
        }
    }
    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getAdvertTermsAndConditions() }
        ) {
            binding.tvInfo.text = " ${Html.fromHtml(it?.fixedPageBody)}"
        }

    }
}