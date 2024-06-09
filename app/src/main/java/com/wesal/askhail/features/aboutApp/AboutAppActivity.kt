package com.wesal.askhail.features.aboutApp

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBarWithWhite
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.databinding.ActivityAboutAppBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AboutAppModel

class AboutAppActivity : BaseActivity() {
    lateinit var binding: ActivityAboutAppBinding;
    override fun layoutResource(): Int =R.layout.activity_about_app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBarWithWhite()
        setToolbar()
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().getAboutApp()}
        ){
            updateUi(it)
        }
    }

    private fun updateUi(model: AboutAppModel?) {
        model?.let {
            binding.tvAppInfo.text = it.appDescription
            binding.viewTwitter.setOnClickListener {_-> IntentsForActions.twitter(this@AboutAppActivity,it.appTwitter) }
            binding.viewSnap.setOnClickListener {_-> IntentsForActions.snapChat(this@AboutAppActivity,it.appSnapchat) }
            binding.tvInstgram.setOnClickListener {_-> IntentsForActions.instgram(this@AboutAppActivity,it.appInstagram) }
            binding.tvWhats.setOnClickListener {_-> IntentsForActions.whatsApp(this@AboutAppActivity,it.appWhatsapp) }
        }
    }
}