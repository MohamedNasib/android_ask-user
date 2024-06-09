package com.wesal.askhail.features.orderDetails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.AdvertStepsTypes
import com.wesal.askhail.core.presentationEnums.AppContentsType
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.core.utilities.ShareManger
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityOrderDetailsBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertSpecificAdapter
import com.wesal.askhail.features.advertDetails.CommentAdapter
import com.wesal.askhail.features.advertDetails.EditContactModel
import com.wesal.askhail.features.advertDetails.EditSpecificationModel
import com.wesal.askhail.features.adverterpage.AdverterPageActivity
import com.wesal.askhail.features.chatActivity.ChatActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepContact.CreateOrderStepContactActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSection.CreateOrderStepSectionActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSpecification.CreateOrderStepSpecificationActivity
import com.wesal.askhail.features.orderComments.OrderCommentsActivity
import com.wesal.askhail.features.visitorLogin.VisitorLoginActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.CommentModel
import com.wesal.entities.models.OrderContentModel


class OrderDetailsActivity : BaseActivity(), CommentAdapter.OnCommentAction {
    lateinit var binding: ActivityOrderDetailsBinding
    private var model: OrderContentModel? = null
    private var orderId: Int = -1
    private val commentAdapter = CommentAdapter(mutableListOf(), this@OrderDetailsActivity)
    private var currentUserId: Int = -1

    override fun layoutResource(): Int = R.layout.activity_order_details
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        orderId = intent.getIntExtra(ExtraConst.EXTRA_ORDER_ID, -1)
        currentUserId = UseCaseImpl().getCurrentUserId()
        val title = getString(R.string.order_no) + " " + orderId
        setToolbar(title)
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
            { UseCaseImpl().getOrderDetails(orderId) }
        ) {
            model = it
            updateUi()
        }
    }
    private fun clickers() {
        binding.viewVisitorChat.setOnClickListener {
            sStartActivity<VisitorLoginActivity>()
        }
        binding.viewOptionEdit.setOnClickListener {
            AppDialogs.editOrderStepsDialog(this) {
                when (it) {
                    AdvertStepsTypes.EDIT_SECTION -> {
                        sStartActivity<CreateOrderStepSectionActivity>(
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_ORDER_ID to orderId,
                            ExtraConst.EXTRA_MAIN_SECTION to model?.orderDetails?.mainSection,
                            ExtraConst.EXTRA_SUB_SECTION to model?.orderDetails?.subSection
                        )
                    }
                    AdvertStepsTypes.EDIT_SPECIFICATION -> {
                        val editSpecificationModel = EditSpecificationModel(
                            model?.orderDetails?.orderTitle!!,
                            model?.orderDetails?.orderDescription!!,
                            "",
                            "",
                            "",
                            model?.orderDetails?.orderPrice!!,
                            model?.orderDetails?.orderSide,
                            model?.orderDetails?.orderBlock,
                            model?.orderDetails?.orderSpecifications!!
                        )
                        sStartActivity<CreateOrderStepSpecificationActivity>(
                            ExtraConst.EXTRA_ORDER_ID to orderId,
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_MODEL to editSpecificationModel
                        )
                    }
                    AdvertStepsTypes.EDIT_COMMUNICATION -> {
                        val editContact = EditContactModel(
                            model?.orderDetails?.orderCallNumber,
                            model?.orderDetails?.orderWhatsappNumber,
                            model?.orderDetails?.orderCallNumberStatus=="active",
                            model?.orderDetails?.orderWhatsappNumberStatus=="active"
                        )
                        sStartActivity<CreateOrderStepContactActivity>(
                            ExtraConst.EXTRA_ORDER_ID to orderId,
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_MODEL to editContact
                        )
                    }
                    else -> {}
                }
            }.show()
        }
        binding.viewOptionDelete.setOnClickListener {
            AppDialogs.redActionDialog(
                this@OrderDetailsActivity,
                getString(R.string.delete_order),
                getString(R.string.delete)
            ){
                performDeleteOrder()
            }.show()
        }

    }

    private fun performDeleteOrder() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().deleteOrder(orderId)}
        ){
            onBackPressed()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        model?.let {
            val questionAdvertiserId = it.orderDetails.orderAdvertiserId
            commentAdapter.ownerUserId = questionAdvertiserId
            /**
             * control basic details
             */
            binding.tvAdvertTitle.text = it.orderDetails.orderTitle
            binding.tvPrice.text = "${it.orderDetails.orderPrice} ${getString(R.string.sar)}"
            binding.ivShare.setOnClickListener { _ -> shareAdvert(it) }
            binding.tvDescription.text = it.orderDetails.orderDescription
            binding.tvCreatedAt.text = "${getString(R.string.createdAt)} ${it.orderDetails.orderCustomPublishedDate}"
            binding.tvUpdatedAt.text = "${getString(R.string.updatedAt)} ${it.orderDetails.orderCustomLastUpdateDate}"
            /**
             * control specification
             */
            val advSpecifications = it.orderDetails.orderSpecifications
            if (advSpecifications.isNullOrEmpty()) {
                binding.viewFeatures.gone()
                binding.rvFeatures.gone()
            } else {
                binding.viewFeatures.visible()
                binding.rvFeatures.visible()
                binding.rvFeatures.layoutManager = GridLayoutManager(this@OrderDetailsActivity, 3)
                binding.rvFeatures.adapter = AdvertSpecificAdapter(advSpecifications.toMutableList(), null)
            }

            /**
             * setUp Location
             */
//            tvAddress.text = it.advertisementDetails.advLocation
 //            tvGoToLocation.setOnClickListener { _ -> goToLocation(it.advertisementDetails.advLatitude, it.advertisementDetails.advLongitude) }
            /**
             * setUp Adverter Details
             */
            binding.tvAdverterName.text = it.orderDetails.orderAdvertiserName
            binding.tvAdvertAdvCount.text =
                "${it.orderDetails.orderAdvertiserAdvsCount} ${getString(R.string.advs)}"
            if (it.orderDetails.orderCallNumberStatus == "block") {
                binding.viewCall.gone()
            } else {
                binding.viewCall.visible()
                binding.viewCall.setOnClickListener { _ -> callNumber(it.orderDetails.orderCallNumber) }
            }
            if (it.orderDetails.orderWhatsappNumberStatus == "block") {
                binding.viewWhats.gone()
            } else {
                binding.viewWhats.visible()
                binding.viewWhats.setOnClickListener { _ -> sendMessageToWhatsApp(it.orderDetails.orderWhatsappNumber) }
            }
            binding.viewChat.setOnClickListener { _ -> openChatWithAdverter() }
            binding.tvGoToAdverter.setOnClickListener { _ ->
                goToAdverterPage(
                    it.orderDetails.orderAdvertiserId,
                    it.orderDetails.orderAdvertiserName,
                    false
                )
            }
            /**
             * control AdvertController Option
             */
            val isCurrentUserOwnerOfAdvert =
                it.orderDetails.orderAdvertiserId == currentUserId
            if (isCurrentUserOwnerOfAdvert) {
                binding.viewOrderController.visible()
                binding.viewSpaceForOptions.visible()
            } else {
                binding.viewOrderController.gone()
                binding.viewSpaceForOptions.gone()
            }

            /**
             * control Comments And Rates
             */
            binding.viewComments.gone()
            val commentsData = it.commentsData
            binding.viewComments.visible()
            binding.tvAddComment.setOnClickListener { _ -> addComment() }
            binding.tvShowMoreComments.setOnClickListener { _ -> showMoreComments() }
            binding.rvComments.layoutManager = LinearLayoutManager(this)
            binding.rvComments.adapter = commentAdapter
            commentAdapter.clearData()
            commentAdapter.addMoreInList(commentsData)
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
            /**
             * control visitor
             */
            if (UseCaseImpl().isInVisitorMode()) {
                binding.viewVisitorChat.visible()
                binding.viewContactingMethods.gone()
                binding.tvGoToAdverter.gone()
            }

        }

    }


    private fun showMoreComments() {
        sStartActivity<OrderCommentsActivity>(
            ExtraConst.EXTRA_ORDER_ID to orderId,
            ExtraConst.EXTRA_ADVERTER_ID to model?.orderDetails?.orderAdvertiserId
        )
    }

    private fun addComment() {
        requiredLoginArea(binding.rvComments,false){
        AppDialogs.createCommentDialog(
            this@OrderDetailsActivity
        ) { comment ->
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().addCommentToOrder(orderId, comment) }
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

    private fun showMoreRates() {
     }

    private fun goToAdverterPage(advAdvertiserId: Int, adverterName: String, business: Boolean) {
        sStartActivity<AdverterPageActivity>(
            ExtraConst.EXTRA_ADVERTER_ID to advAdvertiserId,
            ExtraConst.EXTRA_ADVERTER_NAME to adverterName
        )

    }

    private fun openChatWithAdverter() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {UseCaseImpl().createChat("order",orderId)}
        ){
            it?.let {
                sStartActivity<ChatActivity>(
                    ExtraConst.EXTRA_CHAT_ID to it.chatDetails.chatId
                )
            }
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

    private fun goToLocation(advLatitude: String, advLongitude: String) {
        IntentsForActions.maps(this, advLatitude, advLongitude)
    }

    private fun goToWebLink(advWebsite: String?) {
        advWebsite?.let {
            IntentsForActions.openWebSite(this, it)
        }
    }

    private fun goToFaceBookLink(advFacebook: String?) {
        advFacebook?.let {
            IntentsForActions.facebook(this, it)
        }
    }

    private fun goToSnapLink(advSnapchat: String?) {
        advSnapchat?.let {
            IntentsForActions.snapChat(this, it)
        }
    }

    private fun goToInstaLink(advInstagram: String?) {
        advInstagram?.let {
            IntentsForActions.instgram(this, it)
        }

    }

    private fun goToTwitterLink(advTwitter: String?) {
        advTwitter?.let {
            IntentsForActions.twitter(this, it)
        }
    }

    private fun showMainImage(advPromotionalImage: String) {
        AppDialogs.showBannerDialog(this, advPromotionalImage)
    }

    private fun shareAdvert(mod: OrderContentModel) {
        ShareManger.share(
            this@OrderDetailsActivity,
            AppContentsType.ORDER,
            orderId,
            mod.orderDetails.orderTitle
        )

    }


    override fun onDeleteMyComment(commentModel: CommentModel, boolean: Boolean) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteCommentFromOrder(commentModel.commentId) }
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
                {UseCaseImpl().replayOnOrderComment(commentModel.commentId,replay)}
            ){
                launching()
            }
        }.show()
    }
}
