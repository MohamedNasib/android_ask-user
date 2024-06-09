package com.wesal.askhail.features.highlightAdvert.highlightStepSInfoBank

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.*
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.BanksModel
import com.wesal.entities.models.PackageModel

class HighlightStepSInfoBankActivity : BaseActivity() {
    lateinit var binding: ActivityHighlightStepSInfoBankBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var advertId: Int = -1
    private lateinit var packageModel: PackageModel
    private lateinit var bankModel: BanksModel
    override fun layoutResource(): Int = R.layout.activity_highlight_step_s_info_bank
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighlightStepSInfoBankBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.highlight_advert))
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageModel = intent.getParcelableExtra(ExtraConst.EXTRA_PACKAGE_MODEL)!!
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        bankModel = intent.getParcelableExtra(ExtraConst.EXTRA_BANK_MODEL)!!
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1)
        setUpPackage()
        launchingBankInfo()
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

    private fun launchingBankInfo() {
        ParaNormalProcess.viewProcessActivity(
            this@HighlightStepSInfoBankActivity,
            { UseCaseImpl().getBankInfo(bankModel.bankAccountId) }
        ) {
            it?.let { details ->

                binding.ivBankImage.loadServerImage(details.bankAccountLogo)

                binding.tvBeneficiaryName.text = details.bankAccountUserName
                binding.ivBeneficiaryNameCopy.setOnClickListener {
                    IntentsForActions.copyText(
                        this@HighlightStepSInfoBankActivity,
                        details.bankAccountUserName
                    )
                }

                binding.tvAccountNumber.text = details.bankAccountNumber
                binding.ivAccountNumberCopy.setOnClickListener {
                    IntentsForActions.copyText(
                        this@HighlightStepSInfoBankActivity,
                        details.bankAccountNumber
                    )
                }

                binding.tvIPan.text = details.bankAccountIban
                binding.ivIPanCopy.setOnClickListener {
                    IntentsForActions.copyText(
                        this@HighlightStepSInfoBankActivity,
                        details.bankAccountIban
                    )
                }

            }
        }

    }

    private fun clickers() {
        binding.btnConfirmTransfer.setOnClickListener {
            if (validateRequest()) {
                requestBankTransfer()
            }
        }
    }

    private fun requestBankTransfer() {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        map["package_id"] = packageModel.packageId
        map["display_place"] = packageModel.packageSpecialWith ?: ""
        map["payment_way"] = "bank"
        map["bank_account_id"] = bankModel.bankAccountId
        map["converter_customer_name"] = binding.tieTransferName.value()
        map["converter_bank_name"] = binding.tieBank.value()
        map["converter_account_number"] = binding.tieTransferAccount.value()
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().payHighlightBackObject(map) }
        ) {
            it?.let { q ->

                sStartActivityWithClear<SuccessPageActivity>(
                    ExtraConst.EXTRA_SUCCESS to SuccessRoute.BANK_TRANSFER_HIGHLIGHT,
                    ExtraConst.EXTRA_ADVERT_ID to advertId
                )
            }
        }

    }

    private fun validateRequest(): Boolean {

        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilTransferName, binding.tieTransferName)) {
            binding.tieTransferName.requestFocus();isValid = false
        }

        if (!ValidationLayer.validateEmpty(binding.tilBank, binding.tieBank)) {
            binding.tieBank.requestFocus();isValid = false
        }


        if (!ValidationLayer.validateEmpty(binding.tilTransferAccount, binding.tieTransferAccount)) {
            binding.tieTransferAccount.requestFocus();isValid = false
        }
        return isValid

    }
}