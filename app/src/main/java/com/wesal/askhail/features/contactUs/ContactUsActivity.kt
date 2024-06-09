package com.wesal.askhail.features.contactUs

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityContactUsBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AboutAppModel

class ContactUsActivity : BaseActivity() {
    lateinit var binding: ActivityContactUsBinding;
    override fun layoutResource(): Int = R.layout.activity_contact_us
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.contact_us))
        launching()
        clickers()
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
        map["name"] = binding.tieFullName.value()
        map["email"] = binding.tieEmail.value()
        map["mobile"] = binding.tiePhone.value()
        map["message"] = binding.tieMessage.value()
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().sendContactUs(map)}
        ){
            toasting(R.string.success_request)
            onBackPressed()
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

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getAboutApp() }
        ) {
            updateUi(it)
        }
    }

    private fun updateUi(model: AboutAppModel?) {
        model?.let {
            binding.thPhone.text = it.appMobile
            binding.viewTwitter.setOnClickListener { _ ->
                IntentsForActions.twitter(
                    this@ContactUsActivity,
                    it.appTwitter
                )
            }
            binding.viewSnap.setOnClickListener { _ ->
                IntentsForActions.snapChat(
                    this@ContactUsActivity,
                    it.appSnapchat
                )
            }
            binding.viewInstgram.setOnClickListener { _ ->
                IntentsForActions.instgram(
                    this@ContactUsActivity,
                    it.appInstagram
                )
            }
            binding.viewWhatsApp.setOnClickListener { _ ->
                IntentsForActions.whatsApp(
                    this@ContactUsActivity,
                    it.appWhatsapp
                )
            }
            binding.ivCall.setOnClickListener { _ ->
                IntentsForActions.callNumber(
                    this@ContactUsActivity,
                    it.appMobile
                )
            }
            binding.viewTikTok.setOnClickListener { _ ->
                IntentsForActions.tiktok(
                    this@ContactUsActivity,
                    it.appTikTok
                )
            }
        }
    }
}