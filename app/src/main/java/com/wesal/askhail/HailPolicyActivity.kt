package com.wesal.askhail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityHailPolicyBinding
import com.wesal.askhail.features.more.FixedPagesAdapter
import com.wesal.askhail.features.more.fixedPageDetails.FixedPageDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.FixedPageModel

class HailPolicyActivity : AppCompatActivity(), FixedPagesAdapter.OnFixedPage {
    lateinit var binding: ActivityHailPolicyBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHailPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(getString(R.string.hail_policy))

        UseCaseImpl().loadFixedPages()?.let {
            binding.rvPages.layoutManager = LinearLayoutManager(this)
            val filter = it.toMutableList().filter { q ->
                q.fixedPageSlug != "real-estate-app"
            }
            val adapter = FixedPagesAdapter(filter.toMutableList().take(4).toMutableList(), this@HailPolicyActivity)
            binding.rvPages.adapter = adapter
        }
    }

    override fun onFixedPage(model: FixedPageModel) {
        sStartActivity<FixedPageDetailsActivity>(
            ExtraConst.EXTRA_FIXED_PAGE_SLUG to model.fixedPageSlug,
            ExtraConst.EXTRA_FIXED_PAGE_TITLE to model.fixedPageTitle
        )
    }
}