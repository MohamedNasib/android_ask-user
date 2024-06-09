package com.wesal.askhail.features.subSectionsItems.advertsList

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.requiredLoginArea
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.FragmentAdvertListBinding
import com.wesal.askhail.databinding.FragmentMessagesBinding
import com.wesal.askhail.databinding.FragmentOrdersListBinding
import com.wesal.askhail.databinding.ZOthersBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.advertsOnMap.AdvertOnMapActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepContact.CreateAdvertStepContactActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepInfo.CreateAdvertStepInfoActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia.CreateAdvertStepMediaActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.askhail.features.pickLocation.PickLocationActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.*
import java.util.HashMap

class AdvertsListFragment : BaseFragment(), AdvertsListAdapter.OnAdvertCallBack {
    private var _binding: FragmentAdvertListBinding? = null
    private var _zother_binding: ZOthersBinding? = null
    private var _order_binding: FragmentOrdersListBinding? = null
    private val binding get() = _binding!!
    private val zother_binding get() = _zother_binding!!
    private val order_binding get() = _order_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertListBinding.inflate(inflater, container, false)
        _zother_binding = ZOthersBinding.inflate(inflater, container, false)
        _order_binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    
    private var isBusiness: Boolean = false;
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AdvertsListAdapter? = null
    private var sectionId = -1

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private var filterSort: KeyValueModel? = null
    private var filterBlock: BlocksModel? = null
    private var filterSides: SidesModel? = null
    private var searchQuery :String?= null
    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun layoutRes(): Int = R.layout.fragment_advert_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionId = it.getInt(ExtraConst.EXTRA_SECTION_ID, -1)
            isBusiness = it.getBoolean(ExtraConst.EXTRA_IS_BUSINESS, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdvertsListAdapter(mutableListOf(), isBusiness, this@AdvertsListFragment)
        initViews()
        launching()
        clickers()
    }

    private val requestPermissionLocationLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    context?.sStartActivity<AdvertOnMapActivity>(
                            ExtraConst.EXTRA_SECTION_ID to sectionId
                    )
                } else {

                }
            }

    private fun clickers() {
        _binding?.fabMap?.setOnClickListener {
            val intent = Intent(activity, PickLocationActivity::class.java)
            startActivity(intent)
          // requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        _binding?.fabCreatingStatus?.setOnClickListener {
            requiredLoginArea(order_binding.fabCreateOrder, false) {
                creatingNewAdvertLogic()
            }
        }
    }
    private fun creatingNewAdvertLogic() {
        ParaNormalProcess.loadingProcessFragment(
            this,
            { UseCaseImpl().checkDraftAdverts() }
        ) { checkModel ->
            if (checkModel == null) {
                context?.sStartActivity<CreateAdvertStepInfoActivity>()
            } else {
                AppDialogs.checkDraftDialog(requireActivity()) { isNewAdvert: Boolean ->
                    if (isNewAdvert) {
                        makeNewAdvertId()
                    } else {
                        routeToCreateAdvertStep(checkModel)
                    }
                }.show()
            }
        }
    }
    private fun makeNewAdvertId() {
        ParaNormalProcess.loadingProcessFragment(
            this,
            { UseCaseImpl().makeNewAdvertId() }
        ) {
            context?.sStartActivity<CreateAdvertStepInfoActivity>()
        }
    }
    private fun routeToCreateAdvertStep(checkModel: AdvertStepperModel) {
        val advertisementId = checkModel.advertisementId
        when (checkModel.nextLevel) {
            3 -> context?.sStartActivity<CreateAdvertStepSectionActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            4 -> context?.sStartActivity<CreateAdvertStepMediaActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            5 -> context?.sStartActivity<CreateAdvertStepSpecificationActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            6 -> context?.sStartActivity<CreateAdvertStepContactActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
        }
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvAdverts?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }

        })
        binding.rvAdverts?.layoutManager = layoutManager
        binding.rvAdverts?.adapter = adapter
        binding.rvAdverts.itemAnimator = null
    }

    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessFragment(
                this@AdvertsListFragment,
                { UseCaseImpl().getAdvertsInSubSection(sectionId, currentPage, getFilterMap()) },
            R.drawable.ic_status_empty_order,
            R.string.empty_advert,
            !isBusiness
        ) {
            adapter?.clearData()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
        }
    }

    private fun getMoreItems() {
        currentPage++
        adapter?.addLoadingFooter()
        ParaNormalProcess.silentProcessFragment(
                this@AdvertsListFragment,
                { UseCaseImpl().getAdvertsInSubSection(sectionId, currentPage,getFilterMap()) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }
    }

    override fun onViewAdvertDetails(model: AdvertModel) {
        activity?.sStartActivity<AdvertDetailsActivity>(
                ExtraConst.EXTRA_ADVERT_ID to model.advId
        )
    }

    override fun onToggleFavorite(model: AdvertModel) {
        ParaNormalProcess.silentProcessFragment(
                this,
                { UseCaseImpl().toggleFavoritesAdvert(model.advId) }
        ) {
         }
    }
    fun searchWithText(query:String?){
        searchQuery = query
        launching()

    }
    fun whenFilterClicked() {
        val baseActivity = activity as? BaseActivity
        baseActivity?.let {targetActivity->
            AppDialogs.showFilterDialog(targetActivity,
                    filterSort,
                    filterBlock,
                    filterSides,
                    object : AppDialogs.OnFilterAction {
                        override fun onFilter(selectedSortModel: KeyValueModel?, selectedBlockModel: BlocksModel?, selectedSideModel: SidesModel?) {
                            filterSort = selectedSortModel
                            filterBlock = selectedBlockModel
                            filterSides = selectedSideModel
                            launching()
                        }

                        override fun onClearFilter() {
                            filterSort = null
                            filterBlock = null
                            filterSides = null
                            launching()
                        }

                    }).show()
        }

    }

    private fun getFilterMap(): HashMap<String, Any> {
        val map = hashMapOf<String,Any>()
        filterSort?.let {
            map["order_by"] = it.key
        }
        filterBlock?.let {
            map["block"] = it.blockId
        }
        filterSides?.let {
            map["side"] = it.sideId
        }
        searchQuery?.let {
            map["search"] = it
        }
        return map
    }


}