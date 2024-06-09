package com.wesal.askhail.features.askComments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.PaginationScrollListener
import com.wesal.askhail.databinding.ActivityAdvertCommentsBinding
import com.wesal.askhail.features.advertDetails.CommentAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.CommentModel

class AskCommentsActivity : BaseActivity(), CommentAdapter.OnCommentAction {
    lateinit var binding: ActivityAdvertCommentsBinding;
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var adapter: CommentAdapter? =
        CommentAdapter(mutableListOf(), this@AskCommentsActivity)

    private var askId: Int = -1
    private var adverterId: Int = -2
    override fun layoutResource(): Int = R.layout.activity_advert_comments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertCommentsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        askId = intent.getIntExtra(ExtraConst.EXTRA_ASK_ID, -1)
        adverterId = intent.getIntExtra(ExtraConst.EXTRA_ADVERTER_ID,-2)
        setWhiteActivity()
        setToolbar(getString(R.string.comments))
        val currentUserId = UseCaseImpl().getCurrentUserId()
        adapter?.currentUserId = currentUserId
        adapter?.ownerUserId = adverterId
        initViews()
        launching()
    }
    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvComment.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading
            override fun loadMoreItems() {
                isLoading = true
                getMoreItems()
            }

        })
        binding.rvComment.layoutManager = layoutManager
        binding.rvComment.adapter = adapter
    }
    private fun launching() {
        currentPage = 1
        ParaNormalProcess.viewProcessActivity(
            this@AskCommentsActivity,
            { UseCaseImpl().getQuestionsComments(askId, currentPage) }
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
            this@AskCommentsActivity,
            { UseCaseImpl().getQuestionsComments(askId, currentPage) }
        ) {
            adapter?.removeLoadingFooter()
            adapter?.addMoreInList(it?.data)
            it?.let {
                isLastPage = !it.pagination.hasMorePages
            }
            isLoading = false
        }

    }
    override fun onDeleteMyComment(commentModel: CommentModel, boolean: Boolean) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteCommentFromAsk(commentModel.commentId) }
        ) {
            adapter?.removeComment(commentModel.commentId)
        }
    }

    override fun onCommentReplay(commentModel: CommentModel) {
        AppDialogs.createReplayDialog(
            this,
            commentModel.commentVoterName,
            commentModel.commentText
        ){replay->
            ParaNormalProcess.loadingProcessActivity(
                this,
                {UseCaseImpl().replayOnQuestionComment(commentModel.commentId,replay)}
            ){
                launching()
            }
        }.show()
    }
}