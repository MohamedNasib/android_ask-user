package com.wesal.askhail.features.more.fixedPageDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityFixedPageDetailsBinding
import com.wesal.domain.useCases.UseCaseImpl

class FixedPageDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityFixedPageDetailsBinding
    private var fixedPageTitle: String? = ""
    private var fixedPageSlug: String? = ""

    override fun layoutResource(): Int=R.layout.activity_fixed_page_details
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixedPageDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        fixedPageSlug = intent.getStringExtra(ExtraConst.EXTRA_FIXED_PAGE_SLUG)
        fixedPageTitle = intent.getStringExtra(ExtraConst.EXTRA_FIXED_PAGE_TITLE)
        setToolbar(fixedPageTitle)
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().getFixedPageDetails(fixedPageSlug!!)}
        ){
            binding.tvFixedInfo.text = " ${Html.fromHtml(it?.fixedPageBody)}"
            setUpView()
        }
    }
    private fun setUpView(){
        if (fixedPageSlug =="business-registration"){
            binding.btnConfirm.visible()
            binding.btnConfirm.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.wesal.askhailbusiness")
                    )
                )
            }
        }
        //business-registration
    }
}