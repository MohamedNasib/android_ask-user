package com.wesal.askhail.features.createOrderSteps.createOrderStepContact

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.core.successPage.SuccessPageActivity
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateOrderStepContactBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.advertDetails.EditContactModel
import com.wesal.domain.useCases.UseCaseImpl

class CreateOrderStepContactActivity : BaseActivity() {
    lateinit var binding: ActivityCreateOrderStepContactBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var editModel: EditContactModel?=null
    private var isEdit: Boolean = false

    private var orderId: Int = -1
    private var selectedPhone = ""
    private var selectedWhats = ""
    override fun layoutResource(): Int =R.layout.activity_create_order_step_contact
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrderStepContactBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        orderId = intent.getIntExtra(ExtraConst.EXTRA_ORDER_ID, -1);
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        editModel = intent.getParcelableExtra<EditContactModel>(ExtraConst.EXTRA_MODEL)
        hideStatusBar()
        if (isEdit){
            setToolbar(getString(R.string.edit_advert_contact))
            tool_binding.tvSteps.text = " "
            binding.btnConfirmContact.setText(R.string.save_changes)
            setUpViewForEdit()
        }else{
            setToolbar(getString(R.string.create_new_order))
            tool_binding.tvSteps.text = "4/4"
        }

        setUpView()
        clickers()
    }
    private fun setUpViewForEdit() {
        editModel?.let {
            selectedWhats = it.whats?:""
            selectedPhone= it.phone?:""
            binding.tvPhone.setText(selectedPhone)
            binding.tvWhats.setText(selectedWhats)
            binding.cbContactPhone.isChecked = it.isPhone
            binding.cbContactWhats.isChecked = it.isWhats
        }
    }

    private fun setUpView() {
        if (selectedPhone.isNullOrEmpty()) {
            binding.cbContactPhone.isEnabled = false
        }
        if (selectedWhats.isNullOrEmpty()) {
            binding.cbContactWhats.isEnabled = false
        }

    }

    private fun clickers() {
        binding.tvAddPhone.setOnClickListener {
            AppDialogs.addPhoneDialog(this) { phone ->
                binding.tvPhone.setText(phone)
                selectedPhone = phone
                binding.cbContactPhone.isEnabled = true
                binding.cbContactPhone.isChecked = true
            }.show()
        }
        binding.tvAddWhats.setOnClickListener {
            AppDialogs.addPhoneDialog(this) { phone ->
                binding.tvWhats.setText(phone)
                selectedWhats = phone
                binding.cbContactWhats.isEnabled = true
                binding.cbContactWhats.isChecked = true
            }.show()
        }
        binding.btnConfirmContact.setOnClickListener {
            if (isEdit){
                editRequest()
            }else{
                createRequest()
            }
        }
    }

    private fun createRequest() {
        val map = hashMapOf<String,Any>()
        map["order_id"] = orderId
        if (binding.cbContactPhone.isChecked){
            map["call_contact_status"] = "active"
            map["call_number"] = selectedPhone
        }else{
            map["call_contact_status"] = "block"
        }

        if (binding.cbContactWhats.isChecked){
            map["whatsapp_contact_status"] = "active"
            map["whatsapp_number"] = selectedPhone
        }else{
            map["whatsapp_contact_status"] = "block"
        }
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createOrderStepContact(map)}
        ){
            sStartActivity<SuccessPageActivity>(
                ExtraConst.EXTRA_SUCCESS to SuccessRoute.CREATE_ADVERT
            )
        }

    }
    private fun editRequest() {
        val map = hashMapOf<String,Any>()
        map["order_id"] = orderId
        if (binding.cbContactPhone.isChecked){
            map["call_contact_status"] = "active"
            map["call_number"] = selectedPhone
        }else{
            map["call_contact_status"] = "block"
        }

        if (binding.cbContactWhats.isChecked){
            map["whatsapp_contact_status"] = "active"
            map["whatsapp_number"] = selectedPhone
        }else{
            map["whatsapp_contact_status"] = "block"
        }
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().editOrderStepContact(map)}
        ){
            toasting(R.string.changes_saved,true)
            onBackPressed()
        }

    }
}