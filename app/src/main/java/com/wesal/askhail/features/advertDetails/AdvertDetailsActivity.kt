package com.wesal.askhail.features.advertDetails

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
import com.wesal.askhail.databinding.ActivityAdvertDetailsBinding
import com.wesal.askhail.features.advertComments.AdvertCommentsActivity
import com.wesal.askhail.features.advertHighlightStatus.AdvertHighlightStatusActivity
import com.wesal.askhail.features.advertRates.AdvertRatesActivity
import com.wesal.askhail.features.adverterpage.AdverterPageActivity
import com.wesal.askhail.features.chatActivity.ChatActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepContact.CreateAdvertStepContactActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia.CreateAdvertStepMediaActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.askhail.features.highlightAdvert.highlightStepInfo.HighlightStepInfoActivity
import com.wesal.askhail.features.videoPlayer.VideoPlayerActivity
import com.wesal.askhail.features.visitorLogin.VisitorLoginActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvMediaModel
import com.wesal.entities.models.AdvertContentModel
import com.wesal.entities.models.CommentModel
import com.wesal.entities.models.RateModel

class AdvertDetailsActivity : BaseActivity(),
    AdvertSliderAdapter.OnImageSliderClicked,
    CommentAdapter.OnCommentAction, RateAdapter.OnRateAction {
    lateinit var binding: ActivityAdvertDetailsBinding
    private var model: AdvertContentModel? = null
    private var advertId: Int = -1
    private var currentUserId: Int = -1

    private val commentAdapter = CommentAdapter(mutableListOf(), this@AdvertDetailsActivity)
    private val ratesAdapter = RateAdapter(mutableListOf(), this@AdvertDetailsActivity)

    override fun layoutResource(): Int = R.layout.activity_advert_details
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1)
        currentUserId = UseCaseImpl().getCurrentUserId()
        val title = getString(R.string.advert_no) + " " + advertId
        setToolbar(title)
        setUpInformation()
        launching()
        clickers()
    }


    private fun setUpInformation() {
        val currentUserId = UseCaseImpl().getCurrentUserId()
        commentAdapter.currentUserId = currentUserId
        ratesAdapter.currentUserId = currentUserId

    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getAdvertDetails(advertId) }
        ) {
            model = it
            updateUi()
        }
    }

    private fun clickers() {
        binding.viewVisitorChat.setOnClickListener {
            sStartActivity<VisitorLoginActivity>()
        }
        binding.viewOptionSpecial.setOnClickListener {

            if (model!!.advertisementDetails.advSpecialStatus.isEmpty()) {
                if (model!!.advertisementDetails.isWaiting) {
                    toasting(R.string.please_wait_confirm, true)
                } else {
                    sStartActivity<HighlightStepInfoActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to advertId
                    )
                }
            } else {
                sStartActivity<AdvertHighlightStatusActivity>(
                    ExtraConst.EXTRA_ADVERT_ID to advertId
                )
            }
        }
        binding.viewOptionEdit.setOnClickListener {
            AppDialogs.editAdvertStepsDialog(this) {
                when (it) {
                    AdvertStepsTypes.EDIT_SECTION -> {
                        sStartActivity<CreateAdvertStepSectionActivity>(
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_ADVERT_ID to advertId,
                            ExtraConst.EXTRA_MAIN_SECTION to model?.advertisementDetails?.mainSection,
                            ExtraConst.EXTRA_SUB_SECTION to model?.advertisementDetails?.subSection
                        )
                    }
                    AdvertStepsTypes.EDIT_MEDIA -> {
                        val editModel = EditMediaModel(
                            model?.advertisementDetails?.advPromotionalImage,
                            model!!.advertisementDetails!!.advMedia
                        )
                        sStartActivity<CreateAdvertStepMediaActivity>(
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_MODEL to editModel,
                            ExtraConst.EXTRA_ADVERT_ID to advertId
                        )
                    }
                    AdvertStepsTypes.EDIT_SPECIFICATION -> {
                        val editSpecificationModel = EditSpecificationModel(
                            model?.advertisementDetails?.advTitle!!,
                            model?.advertisementDetails?.advDescription!!,
                            model?.advertisementDetails?.advLocation!!,
                            model?.advertisementDetails?.advLatitude!!,
                            model?.advertisementDetails?.advLongitude!!,
                            model?.advertisementDetails?.advPrice!!,
                            model?.advertisementDetails?.advSide,
                            model?.advertisementDetails?.advBlock,
                            model?.advertisementDetails?.advSpecifications!!
                        )
                        sStartActivity<CreateAdvertStepSpecificationActivity>(
                            ExtraConst.EXTRA_ADVERT_ID to advertId,
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_MODEL to editSpecificationModel
                        )
                    }
                    AdvertStepsTypes.EDIT_COMMUNICATION -> {
                        val editContact = EditContactModel(
                            model?.advertisementDetails?.advCallNumber,
                            model?.advertisementDetails?.advWhatsappNumber,
                            model?.advertisementDetails?.advCallNumberStatus == "active",
                            model?.advertisementDetails?.advWhatsappNumberStatus == "active"
                        )
                        sStartActivity<CreateAdvertStepContactActivity>(
                            ExtraConst.EXTRA_ADVERT_ID to advertId,
                            ExtraConst.EXTRA_IS_EDIT to true,
                            ExtraConst.EXTRA_MODEL to editContact
                        )
                    }
                }
            }.show()
        }
        binding.viewOptionDeActive.setOnClickListener {
            AppDialogs.redActionDialog(
                this@AdvertDetailsActivity,
                getString(R.string.deactive_advert),
                getString(R.string.deactive)
            ) {
                performDeActiveAdvert()
            }.show()
        }
        binding.viewOptionDelete.setOnClickListener {
            AppDialogs.redActionDialog(
                this@AdvertDetailsActivity,
                getString(R.string.delete_advert),
                getString(R.string.delete)
            ) {
                performDeleteAdvert()
            }.show()
        }
    }

    private fun performDeleteAdvert() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteAdvert(advertId) }
        ) {
            onBackPressed()
        }
    }

    private fun performDeActiveAdvert() {

        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().activeOrBlockAdverts(advertId, "block") }
        ) {
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {


        model?.let {

            val questionAdvertiserId = it.advertisementDetails.advAdvertiserId
            commentAdapter.ownerUserId = questionAdvertiserId
            val isBusiness = it.advertisementDetails.advType == "business"
            /**
             * mange special Start Appearance
             */
            if (it.advertisementDetails.advSpecialStatus.isEmpty()) {
                binding.tvIsSpecial.gone()
                binding.ivSpecialOption.setImageResource(R.drawable.ic_option_special)
                binding.tvSpecialOption.setText(R.string.make_special)
            } else {
                binding.ivSpecialOption.setImageResource(R.drawable.ic_option_special_done)
                binding.tvSpecialOption.setText(R.string.the_package)
                binding.tvIsSpecial.visible()
                binding.tvIsSpecial.text = it.advertisementDetails.advSpecialStatus
            }
            binding.ivFav.setImageResource(if (it.advertisementDetails.advIsFavorite) (R.drawable.ic_advert_fav_on) else (R.drawable.ic_advert_fav_off))
            binding.ivFav.setOnClickListener { _ ->
                requiredLoginArea(binding.ivFav, false) {
                    toggleFavIcon();it.advertisementDetails.advIsFavorite =
                    !it.advertisementDetails.advIsFavorite
                }

            }
            if (questionAdvertiserId == currentUserId) {
                binding.ivFav.gone()
            }
            /**
             * control Advert Slider
             */
            binding.vpAdvertSlider.adapter = AdvertSliderAdapter(
                it.advertisementDetails.advMedia.toMutableList(),
                this@AdvertDetailsActivity
            )
            binding.indicatorAdvert.apply {
//                setIndicatorGap(resources.getDimensionPixelOffset(R.dimen._8dp))
//                setIndicatorDrawable(R.drawable.ic_indicator_off, R.drawable.ic_indicator_on)
                setViewPager(binding.vpAdvertSlider)
            }
            /**
             * control basic details
             */
            binding.tvAdvertTitle.text = it.advertisementDetails.advTitle
            binding.tvIsAvailable.gone()
            binding.tvPrice.text = "${it.advertisementDetails.advPrice} ${getString(R.string.sar)}"
            if (it.advertisementDetails.advPrice=="0"){
                binding.tvPrice.gone()
            }else{
                binding.tvPrice.visible()
            }
            binding.tvDistance?.text = "${it.advertisementDetails.advDistance} ${getString(R.string.km)}"
            binding.tvViews.text = " ${it.advertisementDetails.advViews}"
            binding.ivShare.setOnClickListener { _ -> shareAdvert(it) }
            binding.tvDescription.text = it.advertisementDetails.advDescription
            binding.tvCreatedAt.text =
                "${getString(R.string.createdAt)} ${it.advertisementDetails.advCustomPublishedDate}"
            binding.tvUpdatedAt.text =
                "${getString(R.string.updatedAt)} ${it.advertisementDetails.advCustomLastUpdateDate}"
            /**
             * control specification
             */
            val advSpecifications = it.advertisementDetails.advSpecifications
            if (advSpecifications.isNullOrEmpty()) {
                binding.viewFeatures.gone()
                binding.rvFeatures.gone()
            } else {
                binding.viewFeatures.visible()
                binding.rvFeatures.visible()
                binding.rvFeatures.layoutManager = GridLayoutManager(this@AdvertDetailsActivity, 3)
                binding.rvFeatures.adapter = AdvertSpecificAdapter(advSpecifications.toMutableList(), null)
            }
            if (it.advertisementDetails.advPromotionalImage.contains("no_image")) {
                binding.tvMainImage.gone()
            } else {
                binding.tvMainImage.visible()
            }
            binding.tvMainImage.setOnClickListener { _ -> showMainImage(it.advertisementDetails.advPromotionalImage) }
            /**
             * control Business communication links
             */
            if (isBusiness) {
                binding.viewBusinessLinks.visible()

                if (it.advertisementDetails.advTwitter.isNullOrEmpty()) {
                    binding.viewLinkTwitter.gone()
                } else {
                    binding.viewLinkTwitter.visible()
                    binding.tvLinkTwitter.text = it.advertisementDetails.advTwitter
                    binding.viewLinkTwitter.setOnClickListener { _ -> goToTwitterLink(it.advertisementDetails.advTwitter) }
                }

                if (it.advertisementDetails.advInstagram.isNullOrEmpty()) {
                    binding.viewLinkInsta.gone()
                } else {
                    binding.viewLinkInsta.visible()
                    binding.tvLinkInsta.text = it.advertisementDetails.advInstagram
                    binding.viewLinkInsta.setOnClickListener { _ -> goToInstaLink(it.advertisementDetails.advInstagram) }
                }

                if (it.advertisementDetails.advSnapchat.isNullOrEmpty()) {
                    binding.viewLinkSnap.gone()
                } else {
                    binding.viewLinkSnap.visible()
                    binding.tvLinkSnap.text = it.advertisementDetails.advSnapchat
                    binding.viewLinkSnap.setOnClickListener { _ -> goToSnapLink(it.advertisementDetails.advSnapchat) }
                }

                if (it.advertisementDetails.advFacebook.isNullOrEmpty()) {
                    binding.viewLinkFace.gone()
                } else {
                    binding.viewLinkFace.visible()
                    binding.tvLinkFace.text = it.advertisementDetails.advFacebook
                    binding.viewLinkFace.setOnClickListener { _ -> goToFaceBookLink(it.advertisementDetails.advFacebook) }
                }

                if (it.advertisementDetails.advWebsite.isNullOrEmpty()) {
                    binding.viewLinkWeb.gone()
                } else {
                    binding.viewLinkWeb.visible()
                    binding.tvLinkWeb.text = it.advertisementDetails.advWebsite
                    binding.viewLinkWeb.setOnClickListener { _ -> goToWebLink(it.advertisementDetails.advWebsite) }
                }
                if (
                    it.advertisementDetails.advTwitter.isNullOrEmpty() &&
                    it.advertisementDetails.advInstagram.isNullOrEmpty() &&
                    it.advertisementDetails.advSnapchat.isNullOrEmpty() &&
                    it.advertisementDetails.advFacebook.isNullOrEmpty() &&
                    it.advertisementDetails.advWebsite.isNullOrEmpty()
                ) {
                    binding.viewBusinessLinks.gone()
                }

            } else {
                binding.viewBusinessLinks.gone()
            }
            /**
             * setUp Location
             */
            binding.tvAddress.text = it.advertisementDetails.advLocation
            binding.ivMapLocation.loadMapImage(
                it.advertisementDetails.advLatitude,
                it.advertisementDetails.advLongitude
            )
            binding.tvGoToLocation.setOnClickListener { _ ->
                goToLocation(
                    it.advertisementDetails.advLatitude,
                    it.advertisementDetails.advLongitude
                )
            }
            /**
             * setUp Adverter Details
             */
            binding.ivAdverterIcon.setImageResource(if (isBusiness) (R.drawable.ic_adverter_business) else (R.drawable.ic_adverter))
            binding.tvAdverterName.text = it.advertisementDetails.advAdvertiserName
            binding.tvAdvertAdvCount.text =
                "${it.advertisementDetails.advAdvertiserAdvsCount} ${getString(R.string.advs)}"
            if (it.advertisementDetails.advCallNumberStatus == "block") {
                binding.viewCall.gone()
            } else {
                binding.viewCall.visible()
                binding.viewCall.setOnClickListener { _ -> it.advertisementDetails.advCallNumber?.let { it1 ->
                    callNumber(
                        it1
                    )
                } }
            }
            if (it.advertisementDetails.advWhatsappNumberStatus == "block") {
                binding.viewWhats.gone()
            } else {
                binding.viewWhats.visible()
                binding.viewWhats.setOnClickListener { _ -> it.advertisementDetails.advWhatsappNumber?.let { it1 ->
                    sendMessageToWhatsApp(
                        it1
                    )
                } }
            }
            binding.viewChat.setOnClickListener { _ -> openChatWithAdverter() }
            binding.tvGoToAdverter.setOnClickListener { _ ->
                goToAdverterPage(
                    it.advertisementDetails.advAdvertiserId,
                    it.advertisementDetails.advAdvertiserName,
                    isBusiness
                )
            }
            if (questionAdvertiserId == currentUserId) {
                binding.viewAdverter.gone()
            }

            /**
             * control AdvertController Option
             */
            val isCurrentUserOwnerOfAdvert =
                it.advertisementDetails.advAdvertiserId == currentUserId
            if (isCurrentUserOwnerOfAdvert) {
                binding.viewAdvertController.visible()
                binding.viewSpaceForOptions.visible()
            } else {
                binding.viewAdvertController.gone()
                binding.viewSpaceForOptions.gone()
            }


            /**
             * control Comments And Rates
             */
            binding.viewComments.gone()
            binding.viewRates.gone()
            if (isBusiness) {
                binding.viewRates.visible()
                val ratesData = it.ratesData
                binding.tvAddRate.setOnClickListener { _ -> addRate() }
                binding.tvShowMoreRates.setOnClickListener { _ -> showMoreRates() }
                binding.rvRates.layoutManager = LinearLayoutManager(this)
                binding.rvRates.adapter = ratesAdapter
                ratesAdapter.addMoreInList(ratesData)

                if (!ratesData.isNullOrEmpty()) {
                    binding.tvNoRates.gone()
                    binding.tvRatesCount.text = "${getString(R.string.rates)} (${it.ratesCount})"
                    if (it.ratesCount.toInt() <= ratesData.size) {
                        binding.tvShowMoreRates.gone()
                    }
                } else {
                    binding.tvRatesCount.text = "${getString(R.string.rates)} (0)"
                    binding.tvNoRates.visible()
                    binding.tvShowMoreRates.gone()
                }
            } else {
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
                    if (it.commentsCount?.toInt() ?: 0 <= commentsData.size) {
                        binding.tvShowMoreComments.gone()
                    }
                } else {
                    binding.tvCommentsCount.text = "${getString(R.string.comments)} (0)"
                    binding.tvNoComments.visible()
                    binding.tvShowMoreComments.gone()
                }
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

    private fun toggleFavIcon() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().toggleFavoritesAdvert(advertId) }
        ) {
            if (model != null)
                binding.ivFav.setImageResource(if (model!!.advertisementDetails.advIsFavorite) (R.drawable.ic_advert_fav_on) else (R.drawable.ic_advert_fav_off))

        }

    }

    private fun showMoreComments() {
        sStartActivity<AdvertCommentsActivity>(
            ExtraConst.EXTRA_ADVERT_ID to advertId,
            ExtraConst.EXTRA_ADVERTER_ID to model?.advertisementDetails?.advAdvertiserId
        )
    }

    private fun addComment() {
        requiredLoginArea(binding.ivFav, false) {
            AppDialogs.createCommentDialog(
                this@AdvertDetailsActivity
            ) { comment ->
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().addComment(advertId, comment) }
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
        sStartActivity<AdvertRatesActivity>(
            ExtraConst.EXTRA_ADVERT_ID to advertId,
            ExtraConst.EXTRA_ADVERTER_ID to model?.advertisementDetails?.advAdvertiserId
        )
    }

    private fun addRate() {
        requiredLoginArea(binding.ivFav, false) {
            AppDialogs.createRateDialog(
                this@AdvertDetailsActivity
            ) { rate ->
                ParaNormalProcess.loadingProcessActivity(
                    this,
                    { UseCaseImpl().addRate(advertId, rate) }
                ) { u ->
                    u?.let { newRate ->
                        ratesAdapter.addMoreInList(mutableListOf(newRate.rate))
                        binding.tvRatesCount.text =
                            "${getString(R.string.comments)} (${ratesAdapter.itemCount})"
                        binding.tvNoRates.gone()

                    }
                }
            }.show()
        }
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
            { UseCaseImpl().createChat("advertisement", advertId) }
        ) {
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
            IntentsForActions.facebookFullLink(this, it)
        }
    }

    private fun goToSnapLink(advSnapchat: String?) {
        advSnapchat?.let {
            IntentsForActions.snapChatFullLink(this, it)
        }
    }

    private fun goToInstaLink(advInstagram: String?) {
        advInstagram?.let {
            IntentsForActions.instgramFullLink(this, it)
        }

    }

    private fun goToTwitterLink(advTwitter: String?) {
        advTwitter?.let {
            IntentsForActions.twitterFullLink(this, it)
        }
    }

    private fun showMainImage(advPromotionalImage: String) {
        AppDialogs.showBannerDialog(this, advPromotionalImage)
    }

    private fun shareAdvert(mod: AdvertContentModel) {
        ShareManger.share(
            this@AdvertDetailsActivity,
            AppContentsType.ADVERT,
            advertId,
            mod.advertisementDetails.advTitle
        )

    }

    override fun onImageSliderClicked(model: AdvMediaModel) {

        if (!model.mediaVideo.isNullOrEmpty()) {
            sStartActivity<VideoPlayerActivity>(
                "url" to model.mediaVideo
            )
        } else {
            AppDialogs.showBannerDialog(this@AdvertDetailsActivity, model.mediaImage)

        }

    }

    override fun onDeleteMyComment(commentModel: CommentModel, boolean: Boolean) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteComment(commentModel.commentId) }
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
        ) { replay ->
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().replayOnAdvertComment(commentModel.commentId, replay) }
            ) {
                launching()
            }
        }.show()
    }

    override fun onDeleteMyRate(RateModel: RateModel) {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().deleteRate(RateModel.rateId) }
        ) {
            ratesAdapter.removeRate(RateModel.rateId)
            binding.tvRatesCount.text = "${getString(R.string.rates)} (${ratesAdapter.itemCount})"

        }
    }
}