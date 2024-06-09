package com.wesal.askhail.features.createAskHail.createAskHailStepDetails

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAskHailStepDetailsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.domain.useCases.UseCaseImpl

class CreateAskHailStepDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAskHailStepDetailsBinding;
    lateinit var tool_binding: MyToolbarStepsBinding

    private var askId: Int = -1
    private var isEdit: Boolean = false
    private var myTitle: String? = null
    private var description: String? = null
    private var show_name_status: String? = null

    private var selectedImage: String? = null
    override fun layoutResource(): Int = R.layout.activity_create_ask_hail_step_details
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAskHailStepDetailsBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()

        selectedImage = intent.getStringExtra(ExtraConst.EXTRA_IMAGE)
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        askId = intent.getIntExtra(ExtraConst.EXTRA_ASK_ID, -1)
        myTitle = intent.getStringExtra("title")
        description = intent.getStringExtra("description")
        show_name_status = intent.getStringExtra("show_name_status")
        if (isEdit){
            setToolbar(getString(R.string.edit_question_info))
            tool_binding.tvSteps.text = " "
            binding.btnConfirmAsk.setText(R.string.save_changes)
            updateUiForEdit()
        }else{
            setToolbar(getString(R.string.create_new_ask))
            tool_binding.tvSteps.text = "3/3"
        }

        clickers()
    }

    private fun updateUiForEdit() {

        binding.tieAdvertTitle.setText(myTitle)
        binding.tieAdvertDesc.setText(description)
        if (show_name_status=="active"){
            binding.cbIsPublic.isChecked = true
        }

    }

    private fun clickers() {
        binding.btnConfirmAsk?.setOnClickListener {
            if (validateRequest()) {
                if (isEdit){
                    editRequest()
                }else{
                    createRequest()

                }
            }
        }

    }
    private fun editRequest() {
        val status = if (binding.cbIsPublic.isChecked) {"active"} else{"block"}
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().editingQuestion(
                askId,
                null,
                binding.tieAdvertTitle.value(),
                binding.tieAdvertDesc.value(),
                status,
                false
            )}
        ){
            toasting(R.string.changes_saved,true)
            onBackPressed()
        }

    }

    private fun createRequest() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {
                UseCaseImpl().createAskHail(
                    selectedImage,
                    binding.tieAdvertTitle.value(),
                    binding.tieAdvertDesc.value(),
                    binding.cbIsPublic.isChecked
                )
            }
        ) {
            sStartActivityWithClear<SuccessPageActivity>(
                ExtraConst.EXTRA_SUCCESS to SuccessRoute.CREATE_ASK
            )
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilAdvertTitle, binding.tieAdvertTitle)) {
            binding.tieAdvertTitle.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateEmpty(binding.tilAdvertDesc, binding.tieAdvertDesc)) {
            binding.tieAdvertDesc.requestFocus();isValid = false
        }
        return isValid
    }
}