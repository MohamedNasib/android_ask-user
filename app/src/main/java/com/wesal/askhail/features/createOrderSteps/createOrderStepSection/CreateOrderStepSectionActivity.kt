package com.wesal.askhail.features.createOrderSteps.createOrderStepSection

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
import com.wesal.askhail.databinding.ActivityCreateOrderStepSectionBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createOrderSteps.createOrderStepSpecification.CreateOrderStepSpecificationActivity
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.SectionModel

class CreateOrderStepSectionActivity : BaseActivity() {
    lateinit var binding: ActivityCreateOrderStepSectionBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var isEditing: Boolean = false
    private lateinit var mainCategoriesList: List<SectionModel>
    private var selectedMainCategory: SectionModel? = null
    private var selectedSubCategory: SectionModel? = null
    private var orderId: Int = -1
    override fun layoutResource(): Int = R.layout.activity_create_order_step_section
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrderStepSectionBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        orderId = intent.getIntExtra(ExtraConst.EXTRA_ORDER_ID, -1);
        isEditing = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        hideStatusBar()
        if (!isEditing) {
            setToolbar(getString(R.string.create_new_order))
            tool_binding.tvSteps.text = "2/4"
        }else{
            setToolbar(getString(R.string.edit_order_section))
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
                            this@CreateOrderStepSectionActivity,
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
                    editSections()
                }else{
                    setSectionToAdvert()
                }
            }
        }
    }

    private fun setSectionToAdvert() {
        val map = hashMapOf<String, Any>()
        map["order_id"] = orderId
        map["main_section"] = selectedMainCategory?.sectionId!!
        map["sub_section"] = selectedSubCategory?.sectionId!!
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createOrderStepSection(map) }
        ) {
            sStartActivity<CreateOrderStepSpecificationActivity>(
                ExtraConst.EXTRA_ORDER_ID to orderId
            )
        }

    }
    private fun editSections() {
        val map = hashMapOf<String, Any>()
        map["order_id"] = orderId
        map["main_section"] = selectedMainCategory?.sectionId!!
        map["sub_section"] = selectedSubCategory?.sectionId!!
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().editOrderStepSection(map) }
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