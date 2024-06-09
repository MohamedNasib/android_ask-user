package com.wesal.askhail.features.confimPhone

import android.os.Bundle
import android.os.CountDownTimer
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityConfirmPhoneBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.register.RegisterActivity
import com.wesal.askhail.features.resetPassword.ResetPasswordActivity
import com.wesal.domain.useCases.UseCaseImpl

class ConfirmPhoneActivity : BaseActivity() {
    lateinit var binding: ActivityConfirmPhoneBinding;

    private var advertiserId: Int =-1
    private var counterTimer: CountDownTimer?= null

    private lateinit var from: NavPhonePage
    override fun layoutResource(): Int =R.layout.activity_confirm_phone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPhoneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        from = intent.getParcelableExtra(ExtraConst.EXTRA_FROM)!!
        advertiserId = intent.getIntExtra(ExtraConst.EXTRA_ADVERTISER_ID,-1)
        setToolbar()
        setUpView()
        clicker()
        startTimer()

    }
    private fun setUpView() {
        sequenceEditText(binding.etN1, binding.etN2, binding.etN3, binding.etN4, binding.btnNext)

        when(from){
            NavPhonePage.FORGET_PASSWORD -> {
                binding.tvTitle.text = getString(R.string.reset_password)
                binding.tvSubTitle.text = getString(R.string.type_code)
            }
            NavPhonePage.REGISTER -> {
                binding.tvTitle.text = getString(R.string.create_new_account)
                binding.tvSubTitle.text = getString(R.string.type_code)
            }
            NavPhonePage.CHANGE_PHONE -> {}
        }
    }

    private fun clicker() {
        binding.viewResend.setOnClickListener {
            when(from){
                NavPhonePage.FORGET_PASSWORD -> {
                    resendCodeForForgetPassword()

                }
                NavPhonePage.REGISTER -> {
                    resendCodeForRegister()
                }
                NavPhonePage.CHANGE_PHONE -> {}
            }
        }
        binding.btnNext.setOnClickListener {
            if (validateCode()){
                when(from){
                    NavPhonePage.FORGET_PASSWORD ->{confirmCodeForForgetPassword()}
                    NavPhonePage.REGISTER -> {confirmCodeForRegister()}
                    NavPhonePage.CHANGE_PHONE -> {}
                }
            }
        }

    }

    private fun confirmCodeForForgetPassword() {
        val code = binding.etN1.value() + binding.etN2.value() + binding.etN3.value() + binding.etN4.value()
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().confirmCodeForForgetPassword(advertiserId,code)}
        ){
            sStartActivityWithClear<ResetPasswordActivity>(
                ExtraConst.EXTRA_ADVERTISER_ID to advertiserId
            )
        }

    }

    private fun resendCodeForForgetPassword() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().resendCodeForForgetPassword(advertiserId)}
        ){
            startTimer()
        }

    }

    private fun confirmCodeForRegister() {
        val code = binding.etN1.value() + binding.etN2.value() + binding.etN3.value() + binding.etN4.value()
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().confirmCodeForRegister(advertiserId,code)}
        ){
            sStartActivityWithClear<RegisterActivity>(
                ExtraConst.EXTRA_ADVERTISER_ID to advertiserId
            )
        }

    }
    private fun resendCodeForRegister() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().resendCodeForRegister(advertiserId)}
        ){
            startTimer()
        }
    }
    private fun startTimer() {
        binding.viewResend.isClickable = false
        counterTimer = object : CountDownTimer(59 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCounter.text = "${millisUntilFinished / 1000} ${getString(R.string.seconds)}"
            }

            override fun onFinish() {
                binding.tvCounter.text = getString(R.string.now_by_click)
                binding.viewResend.isClickable = true
            }
        }.start()
    }
    private fun validateCode(): Boolean {
        if (!ValidationLayer.validateEmpty(binding.etN1)) return false
        if (!ValidationLayer.validateEmpty(binding.etN2)) return false
        if (!ValidationLayer.validateEmpty(binding.etN3)) return false
        if (!ValidationLayer.validateEmpty(binding.etN4)) return false
        return true
    }

}