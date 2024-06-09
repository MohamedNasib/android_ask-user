package com.wesal.askhail.features.subSectionsItems.ordersList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.requiredLoginArea
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.FragmentOrdersListBinding
import com.wesal.askhail.databinding.ZOthersBinding
import com.wesal.askhail.features.createOrderSteps.createAdvertStepInfo.CreateOrderStepInfoActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepContact.CreateOrderStepContactActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSection.CreateOrderStepSectionActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSpecification.CreateOrderStepSpecificationActivity
import com.wesal.askhail.features.orderDetails.OrderDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.OrderStepperModel
import com.wesal.entities.models.OrdersModel

class OrdersListFragment : BaseFragment(), OrdersListAdapter.OnOrderClicked {
    private var _zothers_binding: ZOthersBinding? = null
    private val zothers_binding get() = _zothers_binding!!
    private var _binding: FragmentOrdersListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        _zothers_binding = ZOthersBinding.inflate(inflater, container, false)
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
    private var adapter: OrdersListAdapter? = null
    private var sectionId = -1
    override fun layoutRes(): Int = R.layout.fragment_orders_list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionId = it.getInt(ExtraConst.EXTRA_SECTION_ID, -1)
            isBusiness = it.getBoolean(ExtraConst.EXTRA_IS_BUSINESS, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrdersListAdapter(mutableListOf(), this@OrdersListFragment)
        initViews()
        launching()
        clickers()
    }


    private fun initViews() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvOrders?.layoutManager = layoutManager
        binding.rvOrders?.adapter = adapter
        binding.rvOrders.itemAnimator = null
    }

    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessFragment(
            this@OrdersListFragment,
            { UseCaseImpl().getOrdersInSubSection(sectionId, currentPage, hashMapOf()) },
            R.drawable.ic_status_empty_order,
            R.string.empty_order,
            true
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
            this@OrdersListFragment,
            { UseCaseImpl().getOrdersInSubSection(sectionId, currentPage, hashMapOf()) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }
    }

    private fun clickers() {
        binding.fabCreateOrder?.setOnClickListener {
            requiredLoginArea(binding.fabCreateOrder, false) {
                checkIfThereOldOrder()
            }
        }
        _binding?.fabCreatingStatus?.setOnClickListener {
            requiredLoginArea(binding.fabCreateOrder, false) {
                checkIfThereOldOrder()
            }
        }
    }

    override fun onOrderClicked(model: OrdersModel) {
        context?.sStartActivity<OrderDetailsActivity>(
            ExtraConst.EXTRA_ORDER_ID to model.orderId
        )

    }

    private fun checkIfThereOldOrder() {
        ParaNormalProcess.loadingProcessFragment(
            this,
            { UseCaseImpl().createOrderStepCheckPage() }
        ) { checkModel ->
            if (checkModel == null) {
                context?.sStartActivity<CreateOrderStepInfoActivity>()
            } else {
                AppDialogs.checkDraftDialog(requireActivity()) { isNewOrder ->
                    if (isNewOrder) {
                        makeNewOrderId()
                    } else {
                        routeToCreateOrderStep(checkModel)
                    }
                }.show()
            }
        }

    }

    private fun routeToCreateOrderStep(checkModel: OrderStepperModel) {
        val orderId = checkModel.orderId
        when (checkModel.nextLevel) {
            2 -> context?.sStartActivity<CreateOrderStepSectionActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
            3 -> context?.sStartActivity<CreateOrderStepSpecificationActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
            4 -> context?.sStartActivity<CreateOrderStepContactActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
        }
    }

    private fun makeNewOrderId() {
        ParaNormalProcess.loadingProcessFragment(
            this,
            { UseCaseImpl().makeNewOrderId() }
        ) {
            if (it != null) {
                context?.sStartActivity<CreateOrderStepSectionActivity>(
                    ExtraConst.EXTRA_ORDER_ID to it.orderId
                )
            }
        }
    }

}