package com.wesal.askhail.features.askHailDetails

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.AdvertStepsTypes
import com.wesal.askhail.core.presentationEnums.AppContentsType
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.ShareManger
import com.wesal.askhail.databinding.ActivityAdvertCommentsBinding
import com.wesal.askhail.databinding.ActivityAskHailDetailsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.CommentAdapter
import com.wesal.askhail.features.askComments.AskCommentsActivity
import com.wesal.askhail.features.createAskHail.createAskHailStepDetails.CreateAskHailStepDetailsActivity
import com.wesal.askhail.features.createAskHail.createAskHailStepMedia.CreateAskHailStepMediaActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AskHailContentModel
import com.wesal.entities.models.CommentModel


class AskHailDetailsActivity : BaseActivity(), CommentAdapter.OnCommentAction {
    lateinit var binding: ActivityAskHailDetailsBinding;

    private var model: AskHailContentModel? = null
    private var askId: Int = -1
    private val commentAdapter = CommentAdapter(mutableListOf(), this@AskHailDetailsActivity)
    private var currentUserId: Int = -1

    override fun layoutResource(): Int = R.layout.activity_ask_hail_details
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskHailDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        askId = intent.getIntExtra(ExtraConst.EXTRA_ASK_ID, -1)
        val title = getString(R.string.ask_no) + " " + askId
        setToolbar(title)
        currentUserId = UseCaseImpl().getCurrentUserId()
        setUpInformation()
        launching()
        clickers()

    }

    private fun setUpInformation() {
        val currentUserId = UseCaseImpl().getCurrentUserId()
        commentAdapter.currentUserId = currentUserId
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getAskDetails(askId) }
        ) {
            model = it
            updateUi()
        }
    }

    private fun clickers() {
        binding.viewOptionEdit.setOnClickListener {
            AppDialogs.editQuestionsStepsDialog(this) {
                when (it) {
                    AdvertStepsTypes.EDIT_MEDIA -> {
                        sStartActivity<CreateAskHailStepMediaActivity>(
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_ASK_ID to askId,
                            ExtraConst.EXTRA_IMAGE to model?.questionDetails?.questionImage,
                            "title" to model?.questionDetails?.questionTitle,
                            "description" to model?.questionDetails?.questionDescription,
                            "show_name_status" to model?.questionDetails?.questionShowNameStatus
                        )
                    }
                    AdvertStepsTypes.EDIT_SPECIFICATION -> {
                        sStartActivity<CreateAskHailStepDetailsActivity>(
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_ASK_ID to askId,
                            ExtraConst.EXTRA_IMAGE to model?.questionDetails?.questionImage,
                            "title" to model?.questionDetails?.questionTitle,
                            "description" to model?.questionDetails?.questionDescription,
                            "show_name_status" to model?.questionDetails?.questionShowNameStatus
                        )
                    }
                    else->{}
                }
            }.show()
        }
        binding.viewOptionDelete.setOnClickListener {
            AppDialogs.redActionDialog(
                this@AskHailDetailsActivity,
                getString(R.string.delete_question),
                getString(R.string.delete)
            ) {
                performDeleteOrder()
            }.show()
        }

    }

    private fun performDeleteOrder() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteQuestion(askId) }
        ) {
            onBackPressed()
        }

    }

    private fun updateUi() {
        model?.let {
            val questionAdvertiserId = it.questionDetails.questionAdvertiserId
            commentAdapter.ownerUserId = questionAdvertiserId
            val questionDetails = it.questionDetails
            if (questionDetails.questionImage.isNullOrEmpty()) {
                binding.ivAskCover.gone()
            } else {
                binding.ivAskCover.visible()
                binding.ivAskCover.loadServerImage(questionDetails.questionImage)
            }
            if (questionDetails.questionShowNameStatus == "active") {
                binding.tvAskerName.visible()
                binding.tvAskerName.text =
                    "${questionDetails.questionAdvertiserName} ${getString(R.string.asks)}"
            } else {
                binding.tvAskerName.gone()
            }
            binding.tvAskTitle.text = questionDetails.questionTitle
            binding.ivAskShare.setOnClickListener {_->
                ShareManger.share(
                    this@AskHailDetailsActivity,
                    AppContentsType.QUESTION,
                    askId,
                    it.questionDetails.questionTitle
                )
            }
            binding.tvDescription.text = questionDetails.questionDescription

            val commentsData = it.commentsData
            if (!commentsData.isNullOrEmpty()) {
                binding.tvNoComments.gone()
                binding.tvCommentsCount.text = "${getString(R.string.comments)} (${it.commentsCount})"
                if (it.commentsCount.toInt() <= commentsData.size){
                    binding.tvShowMoreComments.gone()
                }
            } else {
                binding.tvCommentsCount.text = "${getString(R.string.comments)} (0)"
                binding.tvNoComments.visible()
                binding.tvShowMoreComments.gone()

            }
            binding.tvCreatedAt.text = "${getString(R.string.createdAt)} ${questionDetails.questionCustomPublishedDate}"
            binding.tvUpdatedAt.text = "${getString(R.string.updatedAt)} ${questionDetails.questionCustomLastUpdateDate}"
            commentAdapter.clearData()
            commentAdapter.addMoreInList(commentsData)
            binding.rvComments.layoutManager = LinearLayoutManager(this)
            binding.rvComments.adapter = commentAdapter
            binding.tvAddComment.setOnClickListener { _ -> addComment() }
            binding.tvShowMoreComments.setOnClickListener { _ -> showMoreComments() }

            /**
             * control AdvertController Option
             */
            val isCurrentUserOwnerOfAdvert =
                it.questionDetails.questionAdvertiserId == currentUserId
            if (isCurrentUserOwnerOfAdvert) {
                binding.viewOrderController.visible()
                binding.viewSpaceForOptions.visible()
            } else {
                binding.viewOrderController.gone()
                binding.viewSpaceForOptions.gone()
            }
        }

    }

    private fun addComment() {
        requiredLoginArea(binding.ivAskCover,true){
        AppDialogs.createCommentDialog(
            this@AskHailDetailsActivity
        ) { comment ->
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().addCommentToAsk(askId, comment) }
            ) { u ->
                u?.let { newComment ->
                    commentAdapter.addMoreInList(mutableListOf(newComment.comment))
                    binding.tvCommentsCount.text =
                        "${getString(R.string.comments)} (${commentAdapter.itemCount})"
                    binding.tvNoComments.gone()

                }
            }
        }.show()
        }


    }

    private fun showMoreComments() {
        sStartActivity<AskCommentsActivity>(
            ExtraConst.EXTRA_ASK_ID to askId,
            ExtraConst.EXTRA_ADVERTER_ID to model?.questionDetails?.questionAdvertiserId
        )
    }
    override fun onDeleteMyComment(commentModel: CommentModel, boolean: Boolean) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteCommentFromAsk(commentModel.commentId) }
        ) {
            commentAdapter.removeComment(commentModel.commentId)
            binding.tvCommentsCount.text = "${getString(R.string.comments)} (${commentAdapter.itemCount})"
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