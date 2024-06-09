package com.wesal.askhail.features.myOrders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.customViews.CustomLinearLayoutManager
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityMyOrdersBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.orderDetails.OrderDetailsActivity
import com.wesal.askhail.features.subSectionsItems.ordersList.OrdersListAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.OrdersModel

class MyOrdersActivity : BaseActivity(), OrdersListAdapter.OnOrderClicked {
    lateinit var binding: ActivityMyOrdersBinding
    private var isBusiness: Boolean = false;
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: OrdersListAdapter =OrdersListAdapter(mutableListOf(), this@MyOrdersActivity)
    override fun layoutResource(): Int =R.layout.activity_my_orders
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.acc_myorders))
        initViews()
    }

    override fun onResume() {
        super.onResume()
        launching()
    }

    private fun initViews() {
        val layoutManager = CustomLinearLayoutManager(this)
        binding.rvMyOrders?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvMyOrders?.layoutManager = layoutManager
        binding.rvMyOrders?.adapter = adapter
        binding.rvMyOrders.itemAnimator = null
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getMyOrdersList(currentPage) }
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
            this@MyOrdersActivity,
            { UseCaseImpl().getMyOrdersList(currentPage) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }
    }
    override fun onOrderClicked(model: OrdersModel) {
        sStartActivity<OrderDetailsActivity>(
            ExtraConst.EXTRA_ORDER_ID to model.orderId
        )

    }
}