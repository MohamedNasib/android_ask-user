package com.wesal.askhail.features.highlightAdvert.highlightStepSelectType

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.*
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.highlightAdvert.highlightStepSelectBank.HighlightStepSelectBankActivity
import com.wesal.askhail.features.paymentWebView.PaymentWebViewActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.PackageModel

class HighlightStepSelectTypeActivity : BaseActivity() {
    lateinit var binding: ActivityHighlightStepSelectTypeBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var advertId: Int = -1
    private lateinit var packageModel: PackageModel
    override fun layoutResource(): Int =R.layout.activity_highlight_step_select_type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighlightStepSelectTypeBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.highlight_advert))
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageModel= intent.getParcelableExtra(ExtraConst.EXTRA_PACKAGE_MODEL)!!
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID,-1)
        setUpPackage()
        clickers()
    }
    private fun setUpPackage() {
        packageModel.let {
            binding.tvPackageName?.text = it.packageName
            binding.tvPackageDesc.text = it.packageDescription
            if (it.packagePrice=="0") {
                binding.tvPackagePrice.text = getString(R.string.forfree)
            }else{
                binding.tvPackagePrice.text = it.packagePrice

            }
            binding.tvPackagePeriod.text = it.packageDurationInDays
        }

    }
    private fun clickers() {
        binding.tvPaymentBank.setOnClickListener {
            sStartActivity<HighlightStepSelectBankActivity>(
                ExtraConst.EXTRA_PACKAGE_MODEL to packageModel,
                ExtraConst.EXTRA_ADVERT_ID to advertId
            )
        }
        binding.tvPaymentVisa.setOnClickListener {
            val map = hashMapOf<String,Any>()
            map["advertisement_id"] = advertId
            map["display_place"] = packageModel.packageSpecialWith!!
            map["package_id"] = packageModel.packageId
            map["payment_way"] = "e_payment"
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().payHighlightBackString(map)}
            ){
                it?.let { q->
                    val intent =
                        Intent(this@HighlightStepSelectTypeActivity, PaymentWebViewActivity::class.java)
                    intent.putExtra(ExtraConst.EXTRA_PAYMENT_URL, q)
                    startActivityForResult(intent, 855)
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                855 -> {
                    val booleanExtra = data?.getBooleanExtra("isSuccess", false)
                    if (booleanExtra == true) {
                        toasting(R.string.success_payment)
                        val intExtra = data?.getIntExtra("advertId", -1)

                        TaskStackBuilder.create(applicationContext)
                            .addNextIntentWithParentStack(
                                Intent(this@HighlightStepSelectTypeActivity, AdvertDetailsActivity::class.java)
                                    .putExtra(ExtraConst.EXTRA_ADVERT_ID, advertId)
                            ).startActivities()

//                        AppDialogs.showInfoDialog(
//                            this,
//                            getString(R.string.success_payment),
//                            getString(R.string.success_payment_info),
//                            R.drawable.ic_done_state
//                        ) {
//
//                        }.show()
                    } else {
                        toasting(R.string.error_payment)
//                        AppDialogs.showInfoDialog(
//                            this,
//                            getString(R.string.error_payment),
//                            getString(R.string.error_payment_info),
//                            R.drawable.ic_error_state
//                        ) {
//
//                        }.show()
                    }
                }

            }
        }

    }

}