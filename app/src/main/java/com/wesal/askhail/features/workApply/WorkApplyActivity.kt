package com.wesal.askhail.features.workApply

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ActivityWorkApplyBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.JobsModel

class WorkApplyActivity : BaseActivity() {
    lateinit var binding: ActivityWorkApplyBinding
    private lateinit var model: JobsModel

    override fun layoutResource(): Int =R.layout.activity_work_apply
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkApplyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        model = intent.getParcelableExtra(ExtraConst.EXTRA_MODEL)!!
        setWhiteActivity()
        setToolbar(getString(R.string.work_with_us))
        setUpView()
        clickers()
    }


    private fun setUpView() {

        binding.tvJobName.text = model.jobTitle
        binding.tvJobDescription.text = model.jobDescription
    }

    private fun clickers() {

        binding.btnCont.setOnClickListener {
            if (validateRequest()) {
                sendRequest()
            }
        }
    }

    private fun sendRequest() {

        val map = hashMapOf<String, Any>()
        map["job_id"] = model.jobId
        map["name"] = binding.tieFullName.value()
        map["email"] = binding.tieEmail.value()
        map["mobile"] = binding.tiePhone.value()
        map["message"] = binding.tieMessage.value()
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().applyForJob(map)}
        ){
            sStartActivityWithClear<SuccessPageActivity>(
                ExtraConst.EXTRA_SUCCESS to SuccessRoute.APPLY_JOB
            )
        }
    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilFullName, binding.tieFullName)) {
            binding.tieFullName.requestFocus();isValid = false
        }


        if (!ValidationLayer.validateEmail(binding.tilEmail, binding.tieEmail)) {
            binding.tieEmail.requestFocus();isValid = false
        }


        if (!ValidationLayer.validatePhone(binding.tilPhone, binding.tiePhone)) {
            binding.tiePhone.requestFocus();isValid = false
        }



        if (!ValidationLayer.validateEmpty(binding.tilMessage, binding.tieMessage)) {
            binding.tieMessage.requestFocus();isValid = false
        }

        return isValid
    }


}