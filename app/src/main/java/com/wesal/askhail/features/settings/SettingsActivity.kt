package com.wesal.askhail.features.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.NavPhonePage
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySettingsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.askhail.features.typePhone.TypePhoneActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.SettingsModel
import kotlinx.coroutines.launch

class SettingsActivity : BaseActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun layoutResource(): Int=R.layout.activity_settings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.settings))
        if (UseCaseImpl().isInVisitorMode()){
         setUpVisitorMode()
        }else {
            launching()
        }

        binding.swAllNotification.setOnClickListener {
            if (binding.swAllNotification.getTag().toString().equals("1")) {
                binding.swAllNotification.setTag("0")
                binding.swAllNotification.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
            } else {
                binding.swAllNotification.setTag("1")
                binding.swAllNotification.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
            }
            saveSettings()
        }

        binding.swNewComment.setOnClickListener {
            if (binding.swNewComment.getTag().toString().equals("1")) {
                binding.swNewComment.setTag("0")
                binding.swNewComment.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
            } else {
                binding.swNewComment.setTag("1")
                binding.swNewComment.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
            }
            saveSettings()
        }

        binding.swSpecialMessage.setOnClickListener {
            if (binding.swSpecialMessage.getTag().toString().equals("1")) {
                binding.swSpecialMessage.setTag("0")
                binding.swSpecialMessage.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
            } else {
                binding.swSpecialMessage.setTag("1")
                binding.swSpecialMessage.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
            }
            saveSettings()
        }

        binding.swReplayAsks.setOnClickListener {
            if (binding.swReplayAsks.getTag().toString().equals("1")) {
                binding.swReplayAsks.setTag("0")
                binding.swReplayAsks.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
            } else {
                binding.swReplayAsks.setTag("1")
                binding.swReplayAsks.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
            }
            saveSettings()
        }

        binding.swSaveAds.setOnClickListener {
            if (binding.swSaveAds.getTag().toString().equals("1")) {
                binding.swSaveAds.setTag("0")
                binding.swSaveAds.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
            } else {
                binding.swSaveAds.setTag("1")
                binding.swSaveAds.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
            }
            saveSettings()
        }
    }

    private fun setUpVisitorMode() {
        binding.viewNotificationControl.gone()
        val systemLanguage = UseCaseImpl().getSystemLanguage()
        if (systemLanguage=="ar"){
            binding.tvCurrentLanguage.text = getString(R.string.language_current_arabic)
            binding.tvSwitchToLanguage.text = getString(R.string.switch_language_english)
        }else{
            binding.tvCurrentLanguage.text = getString(R.string.language_current_english)
            binding.tvSwitchToLanguage.text = getString(R.string.switch_language_arabic)
        }
          binding.viewChangeLanguage.setOnClickListener {
            val targetLanguage = if (systemLanguage=="ar") {"en"}else{"ar"}
            UseCaseImpl().changeLanguage(targetLanguage)
            sStartActivityWithClear<SplashActivity>()
        }

    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {
                UseCaseImpl().getSettings()
            }
        ){
           it?.let {
               updateUi(it)
               clickers()
           }
        }

    }

    private fun updateUi(model: SettingsModel) {
        if (model.advertiserAllNotificationsStatus.equals("active")) {
            binding.swAllNotification.setTag(1)
            binding.swAllNotification.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
        } else {
            binding.swAllNotification.setTag(0)
            binding.swAllNotification.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
        }

        if (model.advertiserNewCommentsStatus.equals("active")) {
            binding.swNewComment.setTag(1)
            binding.swNewComment.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
        } else {
            binding.swNewComment.setTag(0)
            binding.swNewComment.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
        }

        if (model.advertiserChatStatus.equals("active")) {
            binding.swSpecialMessage.setTag(1)
            binding.swSpecialMessage.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
        } else {
            binding.swSpecialMessage.setTag(0)
            binding.swSpecialMessage.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
        }

        if (model.advertiserQuestionRepliesStatus.equals("active")) {
            binding.swReplayAsks.setTag(1)
            binding.swReplayAsks.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
        } else {
            binding.swReplayAsks.setTag(0)
            binding.swReplayAsks.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
        }

        if (model.advertiserFavoriteStatus.equals("active")) {
            binding.swSaveAds.setTag(1)
            binding.swSaveAds.setImageDrawable(getDrawable(R.drawable.ic_right_cir))
        } else {
            binding.swSaveAds.setTag(0)
            binding.swSaveAds.setImageDrawable(getDrawable(R.drawable.ic_emp_cir))
        }

        val systemLanguage = UseCaseImpl().getSystemLanguage()
        if (systemLanguage=="ar"){
            binding.tvCurrentLanguage.text = getString(R.string.language_current_arabic)
            binding.tvSwitchToLanguage.text = getString(R.string.switch_language_english)
        }else{
            binding.tvCurrentLanguage.text = getString(R.string.language_current_english)
            binding.tvSwitchToLanguage.text = getString(R.string.switch_language_arabic)
        }

    }

    private fun clickers() {
        binding.viewChangeLanguage.setOnClickListener {
            val systemLanguage = UseCaseImpl().getSystemLanguage()
            val targetLanguage = if (systemLanguage=="ar") {"en"}else{"ar"}
            UseCaseImpl().changeLanguage(targetLanguage)
            saveSettings()
        }
    }

    fun saveSettings() {
        val systemLanguage = UseCaseImpl().getSystemLanguage()
        scopeIO.launch {
            UseCaseImpl().changeSettings(
                if (binding.swAllNotification.getTag().toString().equals("1")) "active" else "block",
                if (binding.swNewComment.getTag().toString().equals("1")) "active" else "block",
                if (binding.swSpecialMessage.getTag().toString().equals("1")) "active" else "block",
                if (binding.swReplayAsks.getTag().toString().equals("1")) "active" else "block",
                if (binding.swSaveAds.getTag().toString().equals("1")) "active" else "block",
                systemLanguage
            )
        }
    }
}