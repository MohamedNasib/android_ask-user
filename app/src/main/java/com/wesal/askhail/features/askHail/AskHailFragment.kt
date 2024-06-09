package com.wesal.askhail.features.askHail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.FragmentAskhailBinding
import com.wesal.askhail.features.askHailDetails.AskHailDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AskHailModel

class AskHailFragment : BaseFragment(), AskHailAdapter.OnAskHail {
    private var _binding: FragmentAskhailBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAskhailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AskHailAdapter? = null
    override fun layoutRes(): Int = R.layout.fragment_askhail
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AskHailAdapter(mutableListOf(), this@AskHailFragment)
        initViews()
        launching()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvAsksHail?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvAsksHail?.layoutManager = layoutManager
        binding.rvAsksHail?.adapter = adapter
        binding.rvAsksHail.itemAnimator = null
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessFragment(
            this@AskHailFragment,
            { UseCaseImpl().getAskHailList(currentPage) },
            R.drawable.ic_status_empty_ask,
            R.string.empty_ask,
            false
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
            this@AskHailFragment,
            { UseCaseImpl().getAskHailList(currentPage) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }
    }

    override fun onAskHail(model: AskHailModel) {
        context?.sStartActivity<AskHailDetailsActivity>(
            ExtraConst.EXTRA_ASK_ID to model.questionId
        )

    }

}