package com.wesal.askhail.features.payPackage.payPackageStepSelectType

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityPayPackageStepSelectTypeBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectBank.PayPackageStepSelectBankActivity
import com.wesal.askhail.features.paymentWebView.PaymentWebViewActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.PackageModel

class PayPackageStepSelectTypeActivity : BaseActivity() {
    lateinit var binding: ActivityPayPackageStepSelectTypeBinding
    private var actionType: Int = -1
    private lateinit var packageModel: PackageModel
    override fun layoutResource(): Int = R.layout.activity_pay_package_step_select_type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayPackageStepSelectTypeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.pay_package_price))
        setWhiteActivity()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageModel = intent.getParcelableExtra(ExtraConst.EXTRA_PACKAGE_MODEL)!!
        actionType = intent.getIntExtra(ExtraConst.EXTRA_ACTION_TYPE, -1)
        setUpPackage()
        clickers()
    }

    private fun setUpPackage() {
        packageModel.let {
            binding.tvPackageName?.text = it.packageName
            binding.tvPackageDesc.text = it.packageDescription
            binding.tvPackageCount.text = it.packageAdvertisementsCount
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
            sStartActivity<PayPackageStepSelectBankActivity>(
                ExtraConst.EXTRA_PACKAGE_MODEL to packageModel,
                ExtraConst.EXTRA_ACTION_TYPE to actionType
            )
            //
        }
        binding.tvPaymentVisa.setOnClickListener {
            if (actionType == -1) {
                val map = hashMapOf<String, Any>()
                map["package_id"] = packageModel.packageId
                map["payment_time"] = "now"
                map["payment_way"] = "e_payment"
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().payPackageBackString(map) }
                ) {
                    it?.let { q ->
                        val intent =
                            Intent(
                                this@PayPackageStepSelectTypeActivity,
                                PaymentWebViewActivity::class.java
                            )
                        intent.putExtra(ExtraConst.EXTRA_PAYMENT_URL, q)
                        startActivityForResult(intent, 855)
                    }
                }
            } else {
                if (actionType == 1) {
                    // for renew package
                    val map = hashMapOf<String, Any>()
                    map["payment_way"] = "e_payment"
                    ParaNormalProcess.loadingProcessActivity(
                        this,
                        { UseCaseImpl().reNewPackageBackString(map) }
                    ) {
                        it?.let { q ->
                            val intent =
                                Intent(
                                    this@PayPackageStepSelectTypeActivity,
                                    PaymentWebViewActivity::class.java
                                )
                            intent.putExtra(ExtraConst.EXTRA_PAYMENT_URL, q)
                            startActivityForResult(intent, 855)
                        }
                    }
                }
                else if (actionType==2){
                    val map = hashMapOf<String, Any>()
                    map["package_id"] = packageModel.packageId
                    map["payment_way"] = "e_payment"
                    ParaNormalProcess.loadingProcessActivity(
                        this,
                        { UseCaseImpl().updatePackageBackString(map) }
                    ) {
                        it?.let { q ->
                            val intent =
                                Intent(
                                    this@PayPackageStepSelectTypeActivity,
                                    PaymentWebViewActivity::class.java
                                )
                            intent.putExtra(ExtraConst.EXTRA_PAYMENT_URL, q)
                            startActivityForResult(intent, 855)
                        }
                    }
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
                        if (actionType == -1) {
                            val intExtra = data?.getIntExtra("advertId", -1)
                            sStartActivityWithClear<CreateAdvertStepSectionActivity>(
                                ExtraConst.EXTRA_ADVERT_ID to intExtra
                            )
                        }else{
                            sStartActivityWithClear<MainActivity>(
                            )
                        }
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