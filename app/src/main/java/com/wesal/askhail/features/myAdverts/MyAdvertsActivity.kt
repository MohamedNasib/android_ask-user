package com.wesal.askhail.features.myAdverts

import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.customViews.CustomLinearLayoutManager
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityMyAdvertsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ZOthersBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepContact.CreateAdvertStepContactActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepInfo.CreateAdvertStepInfoActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia.CreateAdvertStepMediaActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.askhail.features.inactiveAdvertList.InactiveAdvertListActivity
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.AdvertStepperModel

class MyAdvertsActivity : BaseActivity(), AdvertsListAdapter.OnAdvertCallBack {
    lateinit var binding: ActivityMyAdvertsBinding
    lateinit var others_binding: ZOthersBinding
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AdvertsListAdapter? = AdvertsListAdapter(mutableListOf(),false,this)

    override fun layoutResource(): Int =R.layout.activity_my_adverts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAdvertsBinding.inflate(layoutInflater)
        others_binding = ZOthersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.acc_myads))
        initViews()
        clickers()
    }

    private fun clickers() {
        others_binding.fabCreatingStatus?.setOnClickListener {
            creatingNewAdvertLogic()
        }
    }
    private fun creatingNewAdvertLogic() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().checkDraftAdverts() }
        ) { checkModel ->
            if (checkModel == null) {
                sStartActivity<CreateAdvertStepInfoActivity>()
            } else {
                AppDialogs.checkDraftDialog(this) { isNewAdvert: Boolean ->
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

        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().makeNewAdvertId() }
        ) {
            sStartActivity<CreateAdvertStepInfoActivity>()
        }
    }
    private fun routeToCreateAdvertStep(checkModel: AdvertStepperModel) {
        val advertisementId = checkModel.advertisementId
        when (checkModel.nextLevel) {
            3 -> sStartActivity<CreateAdvertStepSectionActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            4 -> sStartActivity<CreateAdvertStepMediaActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            5 -> sStartActivity<CreateAdvertStepSpecificationActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            6 -> sStartActivity<CreateAdvertStepContactActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
        }
    }

    override fun onResume() {
        super.onResume()
        launching()
        binding.tvInActiveAdverts.gone()
    }
    private fun initViews() {
        val layoutManager = CustomLinearLayoutManager(this)
        binding.rvAdvertsList.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }

        })
        binding.rvAdvertsList.layoutManager = layoutManager
        binding.rvAdvertsList.adapter = adapter
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@MyAdvertsActivity,
            { UseCaseImpl().getMyAdverts(currentPage) },
            R.drawable.ic_status_empty_myads,
            R.string.empty_myads,
            true
        ) {
            controlInActiveAdverts(it?.hasInactiveAdverts)
            adapter?.clearData()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
                binding.tvAdvertsCount.text = "${it.pagination.total} ${getString(R.string.advert)}"
            }
        }
    }

    private fun controlInActiveAdverts(hasInactiveAdverts: Boolean?) {
      hasInactiveAdverts?.let {
          if (it){
              binding.tvInActiveAdverts.visible()
              binding.tvInActiveAdverts.text= "${getString(R.string.show_inactive_adverts)}"
              binding.tvInActiveAdverts.setOnClickListener {
                  sStartActivity<InactiveAdvertListActivity>()
              }

          }else{
              binding.tvInActiveAdverts.gone()

          }
      }

    }

    private fun getMoreItems() {
        currentPage++
        adapter?.addLoadingFooter()
        ParaNormalProcess.silentProcessActivity(
            this@MyAdvertsActivity,
            { UseCaseImpl().getMyAdverts(currentPage) }
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
        sStartActivity<AdvertDetailsActivity>(
            ExtraConst.EXTRA_ADVERT_ID to model.advId
        )

    }
    override fun onToggleFavorite(model: AdvertModel) {
        ParaNormalProcess.silentProcessActivity(
            this,
            { UseCaseImpl().toggleFavoritesAdvert(model.advId) }
        ) {
        }
    }
}