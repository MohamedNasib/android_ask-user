package com.wesal.askhail.features.subSections

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivitySubSectionsBinding
import com.wesal.askhail.features.subSectionsItems.SubSectionItemsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.SectionModel

class SubSectionsActivity : BaseActivity(), SubSectionAdapter.OnSubSectionClicked {
    lateinit var binding: ActivitySubSectionsBinding
    private var isBusinessSection: Boolean = false
    private lateinit var sectionModel: SectionModel

    override fun layoutResource(): Int =R.layout.activity_sub_sections
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubSectionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        sectionModel = intent.getParcelableExtra(ExtraConst.EXTRA_SECTION)!!
        isBusinessSection = intent.getBooleanExtra(ExtraConst.EXTRA_IS_BUSINESS,false)
        setToolbar(sectionModel.sectionName)
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().getSubSectionList(sectionModel.sectionId)}
        ){
            it?.let { it1 -> updateUi(it1) }
        }

    }

    private fun updateUi(it: List<SectionModel>) {
        val adapter = SubSectionAdapter(it.toMutableList(),this@SubSectionsActivity)
        binding.rvSubSections.layoutManager = GridLayoutManager(this,2)
        binding.rvSubSections.adapter=adapter
    }

    override fun onSubSectionClicked(model: SectionModel) {
        sStartActivity<SubSectionItemsActivity>(
            ExtraConst.EXTRA_IS_BUSINESS to isBusinessSection,
            ExtraConst.EXTRA_SECTION to model
        )
    }
}