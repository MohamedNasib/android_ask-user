package com.wesal.askhail.features.createOrderSteps.createAdvertStepInfo

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
import com.wesal.askhail.databinding.ActivityCreateOrderStepInfoBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage.CreateAdvertStepPackageActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSection.CreateOrderStepSectionActivity
import com.wesal.domain.useCases.UseCaseImpl

class CreateOrderStepInfoActivity : BaseActivity() {
    lateinit var binding: ActivityCreateOrderStepInfoBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    override fun layoutResource(): Int =R.layout.activity_create_order_step_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrderStepInfoBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()
        setToolbar(getString(R.string.create_new_order))
        tool_binding.tvSteps.text = "1/4"
        launching()
        clickers()
    }
    private fun clickers() {
        binding.btnCont.setOnClickListener {
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().makeNewOrderId() }
            ) {
                if (it != null) {
                    sStartActivity<CreateOrderStepSectionActivity>(
                        ExtraConst.EXTRA_ORDER_ID to it.orderId
                    )
                }
            }
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