package com.wesal.askhail.features.specialAdvertsList

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySpecialAdvertsListBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.BlocksModel
import com.wesal.entities.models.KeyValueModel
import com.wesal.entities.models.SidesModel
import java.util.HashMap

class SpecialAdvertsListActivity : BaseActivity(), AdvertsListAdapter.OnAdvertCallBack {
    lateinit var binding: ActivitySpecialAdvertsListBinding
    private var isBusiness: Boolean = false;

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AdvertsListAdapter? =AdvertsListAdapter(mutableListOf(), true, this@SpecialAdvertsListActivity)
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private var filterSort: KeyValueModel? =null
    private var filterBlock: BlocksModel? =null
    private var filterSides: SidesModel? =null
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun layoutResource(): Int =R.layout.activity_special_adverts_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialAdvertsListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.special_ads))
        initViews()
        launching()
        clickers()
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
        return map
    }
    private fun clickers() {
        binding.ivFilter.setOnClickListener {
            AppDialogs.showFilterDialog(this,
                    filterSort,
                    filterBlock,
                    filterSides,
                    object :AppDialogs.OnFilterAction{
                override fun onFilter(selectedSortModel: KeyValueModel?, selectedBlockModel: BlocksModel?, selectedSideModel: SidesModel?) {
                    filterSort =selectedSortModel
                    filterBlock = selectedBlockModel
                    filterSides =selectedSideModel
                    launching()
                }

                override fun onClearFilter() {
                    filterSort =null
                    filterBlock = null
                    filterSides =null
                    launching()
                }

            }).show()
        }

    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvAdvertsList?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }

        })
        binding.rvAdvertsList?.layoutManager = layoutManager
        binding.rvAdvertsList?.adapter = adapter
        binding.rvAdvertsList.itemAnimator = null
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@SpecialAdvertsListActivity,
            { UseCaseImpl().getSpecialAdvertsList(currentPage, getFilterMap()) }
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
        ParaNormalProcess.silentProcessActivity(
            this@SpecialAdvertsListActivity,
                { UseCaseImpl().getSpecialAdvertsList(currentPage, getFilterMap()) }
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