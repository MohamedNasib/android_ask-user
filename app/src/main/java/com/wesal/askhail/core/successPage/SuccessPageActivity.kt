package com.wesal.askhail.core.successPage

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.SuccessRoute
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.aboutApp.AboutAppActivity
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.splash.SplashActivity

class SuccessPageActivity : BaseActivity() {
    private var advertId: Int = -1
    private lateinit var route: SuccessRoute
    lateinit var binding: ActivitySuccessPageBinding;
    override fun layoutResource(): Int = R.layout.activity_success_page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
        hideStatusBar()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        route = intent.getParcelableExtra(ExtraConst.EXTRA_SUCCESS)!!
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1)
        initIt()
    }

    private fun initIt() {

        when (route) {
            SuccessRoute.REGISTER -> {
                binding.tvMessage.text = getString(R.string.success_reg)
                binding.tvSubMessage.text = getString(R.string.success_reg_info)
            }
            SuccessRoute.CREATE_ADVERT -> {
                binding.tvMessage.text = getString(R.string.success_create_advert)
                binding.tvSubMessage.text = getString(R.string.success_create_advert_info)
            }
            SuccessRoute.CREATE_ORDER -> {
                binding.tvMessage.text = getString(R.string.success_create_order)
                binding.tvSubMessage.text = getString(R.string.success_create_order_info)
            }
            SuccessRoute.CREATE_ASK -> {
                binding.tvMessage.text = getString(R.string.success_create_ask)
                binding.tvSubMessage.text = getString(R.string.success_create_order_ask)
            }
            SuccessRoute.APPLY_JOB -> {
                binding.tvMessage.text = getString(R.string.success_apply_job)
                binding.tvSubMessage.text = getString(R.string.success_apply_job_info)
            }
            SuccessRoute.BANK_TRANSFER_HIGHLIGHT -> {
                binding.tvMessage.text = getString(R.string.success_highlight_bank)
                binding.tvSubMessage.text = getString(R.string.success_highlight_bank_info)
            }
            SuccessRoute.NORMAL_TRANSFER -> {
                binding.tvMessage.text = getString(R.string.success_highlight_bank)
                binding.tvSubMessage.text = getString(R.string.success_highlight_bank_info)
            }
        }
        binding.btnCont.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        navigationDirection()
    }

    private fun navigationDirection() {
        when (route) {
            SuccessRoute.REGISTER -> {
                sStartActivityWithClear<SplashActivity>()
            }
            SuccessRoute.CREATE_ADVERT -> {
                //TODO
                sStartActivityWithClear<MainActivity>()
            }
            SuccessRoute.CREATE_ORDER -> {
                //TODO
                sStartActivityWithClear<MainActivity>()
            }
            SuccessRoute.CREATE_ASK -> {
                //TODO
                sStartActivityWithClear<MainActivity>()
            }
            SuccessRoute.APPLY_JOB -> {
                //TODO
                sStartActivityWithClear<MainActivity>()
            }
            SuccessRoute.BANK_TRANSFER_HIGHLIGHT -> {
                TaskStackBuilder.create(applicationContext)
                    .addNextIntentWithParentStack(
                        Intent(
                            this@SuccessPageActivity,
                            AdvertDetailsActivity::class.java
                        )
                            .putExtra(ExtraConst.EXTRA_ADVERT_ID, advertId)
                    ).startActivities()
            }
            SuccessRoute.NORMAL_TRANSFER -> {
                sStartActivityWithClear<MainActivity>()
            }
        }
    }
}