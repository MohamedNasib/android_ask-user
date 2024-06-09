package com.wesal.askhail.features.changePassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityBrowsingTypeBinding
import com.wesal.askhail.databinding.ActivityChangePasswordBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.domain.useCases.UseCaseImpl

class ChangePasswordActivity : BaseActivity() {
    lateinit var binding: ActivityChangePasswordBinding;
    override fun layoutResource(): Int =R.layout.activity_change_password
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.change_password))

        clickers()
    }

    private fun clickers() {
        binding.btnSaveChanges.setOnClickListener {
            if (validateRequest()){
                makeRequest()
            }
        }

    }

    private fun makeRequest() {
        val map = hashMapOf<String,Any>()
        map["old_password"] = binding.tieCurrentPassword.value()
        map["new_password"] = binding.tieNewPassword.value()
        map["new_password_confirmation"] = binding.tieReNewPassword.value()

        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().changePassword(map)}
        ){
            toasting(R.string.changes_saved,false)
            onBackPressed()
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true


        if (!ValidationLayer.validateLength(binding.tilCurrentPassword, binding.tieCurrentPassword)) {
            binding.tieCurrentPassword.requestFocus();isValid = false
        }

        if (!ValidationLayer.validateLength(binding.tilNewPassword, binding.tieNewPassword)) {
            binding.tieNewPassword.requestFocus();isValid = false
        }

        if (!ValidationLayer.validateMatch(binding.tieReNewPassword,binding.tieNewPassword,binding.tilReNewPassword )) {
            binding.tieReNewPassword.requestFocus();isValid = false
        }

        return isValid


    }
}