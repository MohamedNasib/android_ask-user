package com.wesal.askhail.features.myQuestion

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
import com.wesal.askhail.databinding.ActivityMyQuestionsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ZOthersBinding
import com.wesal.askhail.features.askHail.AskHailAdapter
import com.wesal.askhail.features.askHailDetails.AskHailDetailsActivity
import com.wesal.askhail.features.createAskHail.createAskHailStepInfo.CreateAskHailStepInfoActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AskHailModel

class MyQuestionsActivity : BaseActivity(), AskHailAdapter.OnAskHail {
    lateinit var binding: ActivityMyQuestionsBinding
    lateinit var others_binding: ZOthersBinding
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: AskHailAdapter? = AskHailAdapter(mutableListOf(), this@MyQuestionsActivity)
    override fun layoutResource(): Int =R.layout.activity_my_questions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyQuestionsBinding.inflate(layoutInflater)

        others_binding = ZOthersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.acc_myquestions))
        setWhiteActivity()
        initViews()
        clickers()
    }

    private fun clickers() {
        others_binding.fabCreatingStatus.setOnClickListener {
            sStartActivity<CreateAskHailStepInfoActivity>()
        }

    }

    override fun onResume() {
        super.onResume()
        launching()
    }
    private fun initViews() {
        val layoutManager = CustomLinearLayoutManager(this)
        binding.rvMyQuestions.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }
        })
        binding.rvMyQuestions.layoutManager = layoutManager
        binding.rvMyQuestions.adapter = adapter
        binding.rvMyQuestions.itemAnimator = null
    }

    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@MyQuestionsActivity,
            { UseCaseImpl().getMyQuestions(currentPage) },
            R.drawable.ic_status_empty_myquestions,
            R.string.empty_myquestion,
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
        ParaNormalProcess.silentProcessActivity(
            this@MyQuestionsActivity,
            { UseCaseImpl().getMyQuestions(currentPage) }
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
        sStartActivity<AskHailDetailsActivity>(
            ExtraConst.EXTRA_ASK_ID to model.questionId
        )

    }
}