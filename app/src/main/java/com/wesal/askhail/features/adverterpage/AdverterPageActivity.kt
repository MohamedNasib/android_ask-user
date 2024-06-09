package com.wesal.askhail.features.adverterpage

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityAdvertCommentsBinding
import com.wesal.askhail.databinding.ActivityAdverterPageBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.AdverterInfoModel

class AdverterPageActivity : BaseActivity(), AdvertsListAdapter.OnAdvertCallBack {
    lateinit var binding: ActivityAdverterPageBinding;

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AdvertsListAdapter? = AdvertsListAdapter(mutableListOf(),false,this)

    private var adverterId: Int = -1
    private var adverterName: String? = ""

    override fun layoutResource(): Int = R.layout.activity_adverter_page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdverterPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        adverterId = intent.getIntExtra(ExtraConst.EXTRA_ADVERTER_ID, -1)
        adverterName = intent.getStringExtra(ExtraConst.EXTRA_ADVERTER_NAME)
        setWhiteActivity()
        setToolbar(adverterName)
        initViews()
        launching()
    }
    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvAdverter.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }

        })
        binding.rvAdverter.layoutManager = layoutManager
        binding.rvAdverter.adapter = adapter
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@AdverterPageActivity,
            { UseCaseImpl().getAdverterPage(adverterId, currentPage) }
        ) {
            adapter?.clearData()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
                binding.tvAdvertCount.text = "${it.pagination.total} ${getString(R.string.advert)}"
            }
            controlAdverterInfo(it?.advertiserInfo)

        }
    }
    private fun getMoreItems() {
        currentPage++
        adapter?.addLoadingFooter()
        ParaNormalProcess.silentProcessActivity(
            this@AdverterPageActivity,
            { UseCaseImpl().getAdverterPage(adverterId, currentPage) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }

    }

    private fun controlAdverterInfo(advertiserInfo: AdverterInfoModel?) {
        if (advertiserInfo==null){
            binding.viewAdverterInfo.gone()
        }else{
            binding.viewAdverterInfo.visible()
            binding.viewCall.setOnClickListener { _->callNumber(advertiserInfo.advertiserMobile) }
            binding.viewWhats.setOnClickListener {_->sendMessageToWhatsApp(advertiserInfo.advertiserWhatsapp)  }
        }
    }
    private fun sendMessageToWhatsApp(advWhatsappNumber: String) {
        advWhatsappNumber?.let {
            IntentsForActions.whatsApp(this, it)
        }
    }
    private fun callNumber(advCallNumber: String) {
        advCallNumber?.let {
            IntentsForActions.callNumber(this, it)
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