package com.wesal.askhail.features.createOrderSteps.createOrderStepSpecification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateOrderStepSpecificationBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.advertDetails.EditSpecificationModel
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.SelectAttributeAdapter
import com.wesal.askhail.features.createOrderSteps.createOrderStepContact.CreateOrderStepContactActivity
import com.wesal.askhail.features.pickLocation.PickLocationActivity
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.BlocksModel
import com.wesal.entities.models.SidesModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class CreateOrderStepSpecificationActivity : BaseActivity() {
    lateinit var binding: ActivityCreateOrderStepSpecificationBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var editModel: EditSpecificationModel? = null
    private var isEdit: Boolean = false

    private var lng: Double = 0.0
    private var lat: Double = 0.0
    private var selectedSideModel: SidesModel? = null
    private var sidesList: List<SidesModel>? = null

    private var selectedBlockModel: BlocksModel? = null
    private var blocksList: List<BlocksModel>? = null

    private lateinit var adapter: SelectAttributeAdapter
    private var orderId: Int = -1

    override fun layoutResource(): Int = R.layout.activity_create_order_step_specification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOrderStepSpecificationBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        orderId = intent.getIntExtra(ExtraConst.EXTRA_ORDER_ID, -1);
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        editModel = intent.getParcelableExtra(ExtraConst.EXTRA_MODEL)
        hideStatusBar()
        if (isEdit) {
            setToolbar(getString(R.string.edit_order_specification))
            tool_binding.tvSteps.text = "  "
            binding.btnConfirmSpecification.setText(R.string.save_changes)
            setUpForEdit()
        } else {
            setToolbar(getString(R.string.create_new_order))
            tool_binding.tvSteps.text = "3/4"
        }

        launching()
        clickers()
    }

    private fun setUpForEdit() {
        editModel?.let {
            binding.tieAdvertTitle.setText(it.title)
            binding.tieAdvertDesc.setText(it.description)
//            binding.tieAdvertLocation.setText(it.location)
//            lat = it.lat.toDouble()
//            lng = it.lng.toDouble()
            binding.tieAdvertPrice.setText(it.price.replace(",", ""))
            selectedBlockModel = it.block
            selectedSideModel = it.side

            selectedSideModel?.let { q ->
                binding.tieAdvertSide.setText(q.sideName)
            }
            selectedBlockModel?.let { q ->
                binding.tieAdvertBlock.setText((q.blockName))
            }
        }

    }

    private fun launching() {
//        ParaNormalProcess.viewProcessActivity(
//            this,
//            { UseCaseImpl().getOrderSpecifications(orderId) },
//            skipEmptyView = true
//        ) {
//            it?.let { data ->
//                binding.rvSpecification.layoutManager =
//                    LinearLayoutManager(this@CreateOrderStepSpecificationActivity)
//                adapter = SelectAttributeAdapter(this@CreateOrderStepSpecificationActivity, data)
//                binding.rvSpecification.adapter = adapter
//                if (isEdit) {
//                    editModel?.let { q ->
//                        Timber.e("MATCHING START")
//                        adapter.matchValueForEditing(q.features)
//                    }
//                }
//            }
//        }

    }

    private val requestPermissionLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(this, PickLocationActivity::class.java)
                resultLauncher.launch(intent)
            } else {

            }
        }
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    scopeIO.launch {
                        lat = it.getDoubleExtra(ExtraConst.EXTRA_LAT, 0.0)
                        lng = it.getDoubleExtra(ExtraConst.EXTRA_LNG, 0.0)
                        val toAddress =
                            LatLng(lat, lng).toAddress(this@CreateOrderStepSpecificationActivity)
                        scopeMain.launch {
                            binding.tieAdvertLocation.setText(toAddress)

                        }
                    }
                }
            }
        }

    private fun clickers() {
        binding.btnConfirmSpecification.setOnClickListener {
            if (validateRequest()) {
                if (isEdit) {
                    makeEditRequest()
                } else {
                    makeCreateRequest()
                }
            }
        }
        binding.tieAdvertLocation.setOnClickListener {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        binding.tieAdvertSide.setOnClickListener {
            if (sidesList.isNullOrEmpty()) {
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().getSidesList() }
                ) {
                    sidesList = it
                    selectSides()
                }
            } else {
                selectSides()
            }
        }
        binding.tieAdvertBlock.setOnClickListener {
            if (blocksList.isNullOrEmpty()) {
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().getBlocksList() }
                ) {
                    blocksList = it
                    selectBlock()
                }
            } else {
                selectBlock()
            }
        }

    }

    private fun makeCreateRequest() {
        val map = linkedMapOf<String, Any>()
        map["order_id"] = orderId
        map["title"] = binding.tieAdvertTitle.value()
        map["description"] = binding.tieAdvertDesc.value()
        map["location"] = binding.tieAdvertLocation.value()
        map["lat"] = lat.toString()
        map["lng"] = lng.toString()
        map["if_android"] = true
        map["price"] = binding.tieAdvertPrice.value()
        map["side_id"] = selectedSideModel?.sideId ?: 0

        map["region_id"] = selectedBlockModel?.blockCity?.cityRegion?.regionId ?: 0
        map["city_id"] = selectedBlockModel?.blockCity?.cityId ?: 0
        map["district_id"] = selectedBlockModel?.blockId ?: 0
//        map["block_id"] = selectedBlockModel?.blockId ?: 0

        val featureList = mutableListOf<HashMap<String, Any>>()
        if (this::adapter.isInitialized) {
            adapter.list.forEachIndexed { index, advertFeaturesModel ->
//                map["features[$index]['${advertFeaturesModel.featureId}']"] = advertFeaturesModel.selectedValue
//                map["features[$index]['feature_id']"] = advertFeaturesModel.featureId
//                map["features[$index]['answer']"] = advertFeaturesModel.selectedValue
                val featureMap = hashMapOf<String, Any>()
                featureMap["feature_id"] = advertFeaturesModel.featureId
                featureMap["answer"] = advertFeaturesModel.selectedValue
                featureList.add(featureMap)
            }
        }
        map["features"] = featureList
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createOrderStepFeature(map) }
        ) {
            sStartActivity<CreateOrderStepContactActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
            finish()
        }
    }

    private fun makeEditRequest() {
        val map = linkedMapOf<String, Any>()
        map["order_id"] = orderId
        map["title"] = binding.tieAdvertTitle.value()
        map["description"] = binding.tieAdvertDesc.value()
        map["location"] = binding.tieAdvertLocation.value()
        map["lat"] = lat.toString()
        map["lng"] = lng.toString()
        map["if_android"] = true
        map["price"] = binding.tieAdvertPrice.value()
        map["side_id"] = selectedSideModel?.sideId ?: 0
        map["region_id"] = selectedBlockModel?.blockCity?.cityRegion?.regionId ?: 0
        map["city_id"] = selectedBlockModel?.blockCity?.cityId ?: 0
        map["district_id"] = selectedBlockModel?.blockId ?: 0
//        map["block_id"] = selectedBlockModel?.blockId ?: 0
        val featureList = mutableListOf<HashMap<String, Any>>()
        if (this::adapter.isInitialized) {
            adapter.list.forEachIndexed { index, advertFeaturesModel ->
//                map["features[$index]['${advertFeaturesModel.featureId}']"] = advertFeaturesModel.selectedValue
//                map["features[$index]['feature_id']"] = advertFeaturesModel.featureId
//                map["features[$index]['answer']"] = advertFeaturesModel.selectedValue
                val featureMap = hashMapOf<String, Any>()
                featureMap["feature_id"] = advertFeaturesModel.featureId
                if (advertFeaturesModel.featureType=="choose"){
                    try {
                        featureMap["answer"] = advertFeaturesModel.selectedValue.toInt()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }else{
                    featureMap["answer"] = advertFeaturesModel.selectedValue

                }
                featureList.add(featureMap)
            }
        }
        map["features"] = featureList
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().editOrderStepFeature(map) }
        ) {
            toasting(R.string.changes_saved, true)
            onBackPressed()
        }
    }

    private fun selectSides() {
        sidesList?.let {
            SingleChoiceDialog(
                this,
                getString(R.string.advert_side),
                it,
                object : OnSingleDialogSelected<SidesModel> {
                    override fun onSelected(value: SidesModel) {
                        selectedSideModel = value
                        binding.tieAdvertSide.setText(value.sideName)
                    }
                }
            )
        }

    }

    private fun selectBlock() {
        blocksList?.let {
            SingleChoiceDialog(
                this,
                getString(R.string.advert_block),
                it,
                object : OnSingleDialogSelected<BlocksModel> {
                    override fun onSelected(value: BlocksModel) {
                        selectedBlockModel = value
                        binding.tieAdvertBlock.setText(value.blockName)
                    }
                }
            )
        }

    }

    private fun validateRequest(): Boolean {
        var isValid = true
        if (!ValidationLayer.validateEmpty(binding.tilAdvertTitle, binding.tieAdvertTitle)) {
            binding.tieAdvertTitle.requestFocus();isValid = false
        }
        if (!ValidationLayer.validateEmpty(binding.tilAdvertDesc, binding.tieAdvertDesc)) {
            binding.tieAdvertDesc.requestFocus();isValid = false
        }
        //location
        if (lat == 0.0) {
            toasting(R.string.advert_location)
            isValid = false
        }
        if (!ValidationLayer.validateEmpty(binding.tilAdvertPrice, binding.tieAdvertPrice)) {
            binding.tieAdvertPrice.requestFocus();isValid = false
        }
        if (selectedSideModel == null) {
            toasting(R.string.select_slide)
            isValid = false
        }
        if (selectedBlockModel == null) {
            toasting(R.string.select_block)
            isValid = false
        }
        if (this::adapter.isInitialized) {
            val valid = adapter.isValid()
            if (!valid) {
                isValid = false
            }
        }
        return isValid


    }
}