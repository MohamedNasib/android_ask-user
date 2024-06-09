package com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.hideStatusBar
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAdvertStepSectionBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia.CreateAdvertStepMediaActivity
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.SectionModel

class CreateAdvertStepSectionActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAdvertStepSectionBinding;
    lateinit var tool_binding: MyToolbarStepsBinding
    private var isEditing: Boolean = false
    private lateinit var mainCategoriesList: List<SectionModel>
    private var selectedMainCategory: SectionModel? = null
    private var selectedSubCategory: SectionModel? = null
    private var advertId: Int = -1
    override fun layoutResource(): Int = R.layout.activity_create_advert_step_section
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertStepSectionBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1);
        isEditing = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        hideStatusBar()
        if (!isEditing){
            setToolbar(getString(R.string.create_new_advert))
            tool_binding.tvSteps.text = "3/6"
            binding.btnConfirmSection.text = getString(R.string.confirm_section_type)
        }else{
            setToolbar(getString(R.string.edit_advert_section))
            tool_binding.tvSteps.text = "   "
            binding.btnConfirmSection.text = getString(R.string.save_changes)
            selectedMainCategory=  intent.getParcelableExtra(ExtraConst.EXTRA_MAIN_SECTION)
            selectedSubCategory = intent.getParcelableExtra(ExtraConst.EXTRA_SUB_SECTION)
            setUpViewForEditing()
        }

        launchingMainCategories()
        clickers()
    }

    private fun setUpViewForEditing() {
        selectedMainCategory?.let {
            binding.tieMainSection.setText(it.sectionName)
        }
        selectedSubCategory?.let {
            binding.tieSubSection.setText(it.sectionName)
        }
    }

    private fun clickers() {
        binding.tieMainSection.setOnClickListener {
            SingleChoiceDialog(
                this,
                getString(R.string.section),
                mainCategoriesList,
                object : OnSingleDialogSelected<SectionModel> {
                    override fun onSelected(value: SectionModel) {
                        selectedMainCategory = value
                        binding.tieMainSection.setText(selectedMainCategory!!.sectionName)
                        selectedSubCategory = null
                        binding.tieSubSection.setText(null)
                    }

                }
            )
        }
        binding.tieSubSection.setOnClickListener {
            if (selectedMainCategory == null) {
                toasting(R.string.select_main_category_first)
            } else {
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().getSubMainCategories(selectedMainCategory!!.sectionId) }
                ) {
                    it?.let { q ->
                        SingleChoiceDialog(
                            this@CreateAdvertStepSectionActivity,
                            getString(R.string.subsection),
                            q,
                            object : OnSingleDialogSelected<SectionModel> {
                                override fun onSelected(value: SectionModel) {
                                    selectedSubCategory = value
                                    binding.tieSubSection.setText(selectedSubCategory!!.sectionName)
                                }

                            }
                        )
                    }
                }
            }
        }
        binding.btnConfirmSection.setOnClickListener {
            if (validateRequest()) {
                if (isEditing){
                    editAdvertSection()
                }else{
                    setSectionToAdvert()

                }
            }
        }
    }
    private fun setSectionToAdvert() {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        map["main_section"] = selectedMainCategory?.sectionId!!
        map["sub_section"] = selectedSubCategory?.sectionId!!
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createAdvertStepSection(map) }
        ) {
            sStartActivity<CreateAdvertStepMediaActivity>(
                ExtraConst.EXTRA_ADVERT_ID to advertId
            )
        }

    }
    private fun editAdvertSection() {
        val map = hashMapOf<String, Any>()
        map["advertisement_id"] = advertId
        map["main_section"] = selectedMainCategory?.sectionId!!
        map["sub_section"] = selectedSubCategory?.sectionId!!
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().editAdvertStepSection(map) }
        ) {
            toasting(R.string.changes_saved,true)
            onBackPressed()
        }

    }
    private fun validateRequest(): Boolean {
        if (selectedMainCategory == null) {
            toasting(R.string.select_main_category_first)
            return false
        }
        if (selectedSubCategory == null) {
            toasting(R.string.subsection)
            return false
        }
        return true
    }
    private fun launchingMainCategories() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getMainCategories() }
        ) {
            mainCategoriesList = it!!
        }
    }
}