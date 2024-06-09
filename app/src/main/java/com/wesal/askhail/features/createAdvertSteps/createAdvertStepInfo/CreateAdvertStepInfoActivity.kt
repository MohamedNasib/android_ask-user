package com.wesal.askhail.features.createAdvertSteps.createAdvertStepInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAdvertStepInfoBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage.CreateAdvertStepPackageActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.domain.useCases.UseCaseImpl

class CreateAdvertStepInfoActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAdvertStepInfoBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    override fun layoutResource(): Int = R.layout.activity_create_advert_step_info
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertStepInfoBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()
        setToolbar(getString(R.string.create_new_advert))
        tool_binding.tvSteps.text = "1/6"
        launching()
        clickers()
    }

    private fun clickers() {
        binding.btnCont.setOnClickListener {
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().createAdvertStepCheckPage() }
            ) {
                if (it == null) {
                    sStartActivity<CreateAdvertStepPackageActivity>()
                    finish()
                } else {
                    sStartActivity<CreateAdvertStepSectionActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to it.advertisementId
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