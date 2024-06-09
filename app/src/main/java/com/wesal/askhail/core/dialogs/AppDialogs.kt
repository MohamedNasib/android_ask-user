package com.wesal.askhail.core.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.AdvertStepsTypes
import com.wesal.askhail.core.presentationEnums.AppContentsType
import com.wesal.askhail.core.presentationEnums.PayingTimeType
import com.wesal.askhail.core.utilities.ConstantsObjects
import com.wesal.askhail.core.utilities.ValidationLayer
import com.wesal.askhail.subFeatures.dialogSingle.OnSingleDialogSelected
import com.wesal.askhail.subFeatures.dialogSingle.SingleChoiceDialog
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.BlocksModel
import com.wesal.entities.models.KeyValueModel
import com.wesal.entities.models.SidesModel
import com.willy.ratingbar.ScaleRatingBar

object AppDialogs {

    fun createCommentDialog(context: Activity, callback: (comment: String) -> Unit): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );

        dialog.setContentView(R.layout.dialog_create_comment)
        dialog.setCanceledOnTouchOutside(true)
        val tvAddComment = dialog.findViewById<TextView>(R.id.tvAddComment)
        val tilComment = dialog.findViewById<TextInputLayout>(R.id.tilComment)
        val tieComment = dialog.findViewById<TextInputEditText>(R.id.tieComment)
        tvAddComment?.setOnClickListener {
            if (ValidationLayer.validateEmpty(tilComment, tieComment)) {
                dialog.dismiss()
                callback.invoke(tieComment.value())
            }

        }

        return dialog
    }
    fun createReplayDialog(context: Activity,commenterName:String,comment:String, callback: (replay: String) -> Unit): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );

        dialog.setContentView(R.layout.dialog_create_replay)
        dialog.setCanceledOnTouchOutside(true)
        val tvCommenterName = dialog.findViewById<TextView>(R.id.tvCommenterName)
        val tvComment = dialog.findViewById<TextView>(R.id.tvComment)
        tvCommenterName.text = "$commenterName ${context.getString(R.string.says)}"
        tvComment.text = comment


        val tvAddComment = dialog.findViewById<TextView>(R.id.tvAddComment)
        val tilComment = dialog.findViewById<TextInputLayout>(R.id.tilComment)
        val tieComment = dialog.findViewById<TextInputEditText>(R.id.tieComment)
        tvAddComment?.setOnClickListener {
            if (ValidationLayer.validateEmpty(tilComment, tieComment)) {
                dialog.dismiss()
                callback.invoke(tieComment.value())
            }

        }

        return dialog
    }
    fun createNewHomeDialog(
        context: Activity,
        callback: (type: AppContentsType) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_create_new)
        dialog.setCanceledOnTouchOutside(true)
        val viewAddAdvert = dialog.findViewById<View>(R.id.viewAddAdvert)
        val viewAddOrder = dialog.findViewById<View>(R.id.viewAddOrder)
        val viewAddQuestion = dialog.findViewById<View>(R.id.viewAddQuestion)
        viewAddAdvert.setOnClickListener { dialog.dismiss();callback.invoke(AppContentsType.ADVERT) }
        viewAddOrder.setOnClickListener { dialog.dismiss();callback.invoke(AppContentsType.ORDER) }
        viewAddQuestion.setOnClickListener { dialog.dismiss();callback.invoke(AppContentsType.QUESTION) }
        return dialog
    }
    fun searchDialog(context: Activity,callback: (search: String) -> Unit):Dialog{
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );

        dialog.setContentView(R.layout.dialog_search)
        dialog.setCanceledOnTouchOutside(true)
        val btnSearch = dialog.findViewById<Button>(R.id.btnSearch)
        val tilComment = dialog.findViewById<TextInputLayout>(R.id.tilComment)
        val tieComment = dialog.findViewById<TextInputEditText>(R.id.tieComment)
        btnSearch?.setOnClickListener {
            if (ValidationLayer.validateEmpty(tilComment, tieComment)) {
                dialog.dismiss()
                callback.invoke(tieComment.value())
            }

        }
        return dialog
    }
    fun redActionDialog(
        context: Activity,
        title:String,
        actionTitle:String,
        callback: () -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_red_action)
        dialog.setCanceledOnTouchOutside(true)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = title
        val tvActionName = dialog.findViewById<TextView>(R.id.tvActionName)
        tvActionName.text = actionTitle

        val viewDeActive = dialog.findViewById<View>(R.id.viewDeActive)
        val viewCancel = dialog.findViewById<View>(R.id.viewCancel)
        viewDeActive.setOnClickListener { dialog.dismiss();callback.invoke() }
        viewCancel.setOnClickListener { dialog.dismiss()}
        return dialog
    }
    fun activeAdvertDialog(
        context: Activity,
        callback: () -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_active_advert)
        dialog.setCanceledOnTouchOutside(true)
        val viewDeActive = dialog.findViewById<View>(R.id.viewDeActive)
        val viewCancel = dialog.findViewById<View>(R.id.viewCancel)
        viewDeActive.setOnClickListener { dialog.dismiss();callback.invoke() }
        viewCancel.setOnClickListener { dialog.dismiss()}
        return dialog
    }

    fun editAdvertStepsDialog(
        context: Activity,
        callback: (type: AdvertStepsTypes) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_edit_advert_steps)
        dialog.setCanceledOnTouchOutside(true)
        val viewEditSection = dialog.findViewById<View>(R.id.viewEditSection)
        val viewEditMedia = dialog.findViewById<View>(R.id.viewEditMedia)
        val viewEditSpecification = dialog.findViewById<View>(R.id.viewEditSpecification)
        val viewEditCommunication = dialog.findViewById<View>(R.id.viewEditCommunication)
        viewEditSection.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_SECTION) }
        viewEditMedia.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_MEDIA) }
        viewEditSpecification.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_SPECIFICATION) }
        viewEditCommunication.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_COMMUNICATION) }
        return dialog
    }


    fun editOrderStepsDialog(
        context: Activity,
        callback: (type: AdvertStepsTypes) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_edit_order_steps)
        dialog.setCanceledOnTouchOutside(true)
        val viewEditSection = dialog.findViewById<View>(R.id.viewEditSection)
        val viewEditSpecification = dialog.findViewById<View>(R.id.viewEditSpecification)
        val viewEditCommunication = dialog.findViewById<View>(R.id.viewEditCommunication)
        viewEditSection.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_SECTION) }
        viewEditSpecification.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_SPECIFICATION) }
        viewEditCommunication.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_COMMUNICATION) }
        return dialog
    }

    fun editQuestionsStepsDialog(
        context: Activity,
        callback: (type: AdvertStepsTypes) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_edit_question_steps)
        dialog.setCanceledOnTouchOutside(true)
        val viewEditImage = dialog.findViewById<View>(R.id.viewEditImage)
        val viewEditDesc = dialog.findViewById<View>(R.id.viewEditDesc)
        viewEditImage.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_MEDIA) }
        viewEditDesc.setOnClickListener { dialog.dismiss();callback.invoke(AdvertStepsTypes.EDIT_SPECIFICATION) }
        return dialog
    }

    fun paymentOptionsDialog(
        context: Activity,
        callback: (type: PayingTimeType) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_payment_options)
        dialog.setCanceledOnTouchOutside(true)
        val viewPayNow = dialog.findViewById<View>(R.id.viewPayNow)
        val viewPayLater = dialog.findViewById<View>(R.id.viewPayLater)
        viewPayNow.setOnClickListener { dialog.dismiss();callback.invoke(PayingTimeType.PAY_NOW) }
        viewPayLater.setOnClickListener { dialog.dismiss();callback.invoke(PayingTimeType.PAY_LATER) }
        return dialog
    }

    fun addPhoneDialog(
        context: Activity,
        callback: (phone: String) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_create_phone)
        dialog.setCanceledOnTouchOutside(true)
        val tilPhone = dialog.findViewById<TextInputLayout>(R.id.tilPhone)
        val tiePhone = dialog.findViewById<TextInputEditText>(R.id.tiePhone)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)

        btnConfirm.setOnClickListener {
            if (ValidationLayer.validatePhone(tilPhone, tiePhone)) {
                dialog.dismiss()
                callback.invoke(tiePhone.value())
            }
        }

        return dialog
    }

    fun checkDraftDialog(
        context: Activity,
        callback: (isNewAdvert: Boolean) -> Unit
    ): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_check_draft)
        dialog.setCanceledOnTouchOutside(true)
        val viewCreateNew = dialog.findViewById<View>(R.id.viewCreateNew)
        val viewCont = dialog.findViewById<View>(R.id.viewCont)
        viewCreateNew.setOnClickListener { dialog.dismiss();callback.invoke(true) }
        viewCont.setOnClickListener { dialog.dismiss();callback.invoke(false) }
        return dialog
    }

    fun createRateDialog(context: Activity, callback: (rate: String) -> Unit): Dialog {
        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_create_rate)
        dialog.setCanceledOnTouchOutside(true)
        val rateBar = dialog.findViewById<ScaleRatingBar>(R.id.rateBar)
        val tvAddRate = dialog.findViewById<TextView>(R.id.tvAddRate)
        tvAddRate?.setOnClickListener {
            dialog.dismiss()
            callback.invoke(rateBar.rating.toInt().toString())
        }
        return dialog
    }

    fun showBannerDialog(activity: Activity, imageUrl: String) {
        val dialog = Dialog(activity, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_banner)
        dialog.setCanceledOnTouchOutside(true)
        val ivBanner = dialog.findViewById<ImageView>(R.id.ivBanner)
        ivBanner.loadServerImage(imageUrl)
        ivBanner.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    interface OnFilterAction{
        fun onFilter(selectedSortModel: KeyValueModel?,selectedBlockModel: BlocksModel?,selectedSideModel: SidesModel?)
        fun onClearFilter()
    }
    fun showFilterDialog(context: BaseActivity,
                         preSelectedSortModel: KeyValueModel?,
                         preSelectedBlockModel: BlocksModel?,
                         preSelectedSideModel: SidesModel?
                         ,callBack:OnFilterAction): Dialog {
        var selectedSideModel: SidesModel? = preSelectedSideModel
        var sidesList: List<SidesModel>? = null

        var selectedBlockModel: BlocksModel? = preSelectedBlockModel
        var blocksList: List<BlocksModel>? = null

        var selectedSortModel: KeyValueModel? = preSelectedSortModel
        var sortList: List<KeyValueModel> = ConstantsObjects.getSortingList(context)

        val dialog = Dialog(context, R.style.dialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        dialog.setContentView(R.layout.dialog_filter)
        dialog.setCanceledOnTouchOutside(true)
        val tieSortBy = dialog.findViewById<TextInputEditText>(R.id.tieSortBy)
        selectedSortModel?.let {
            tieSortBy.setText(it.name)
        }
        val tieAdvertSide = dialog.findViewById<TextInputEditText>(R.id.tieAdvertSide)
        selectedSideModel?.let {
            tieAdvertSide.setText(it.sideName)
        }
        val tieAdvertBlock = dialog.findViewById<TextInputEditText>(R.id.tieAdvertBlock)
        selectedBlockModel?.let {
            tieAdvertBlock.setText(it.blockName)
        }
        val btnFilter = dialog.findViewById<Button>(R.id.btnFilter)
        val tvClearFilter = dialog.findViewById<TextView>(R.id.tvClearFilter)
        tieSortBy.setOnClickListener {
            SingleChoiceDialog(
                    context,
                    context.getString(R.string.advert_side),
                    sortList,
                    object : OnSingleDialogSelected<KeyValueModel> {
                        override fun onSelected(value: KeyValueModel) {
                            selectedSortModel=value
                            tieSortBy.setText(selectedSortModel?.name)
                        }
                    }
            )
        }
        tieAdvertSide.setOnClickListener {
            if (sidesList.isNullOrEmpty()) {
                ParaNormalProcess.loadingProcessActivity(
                    context,
                    { UseCaseImpl().getSidesList() }
                ) {
                    sidesList = it
                    selectSides(context, sidesList) { sm ->
                        selectedSideModel = sm
                        tieAdvertSide.setText(selectedSideModel?.sideName)
                    }
                }
            } else {
                selectSides(context, sidesList) { sm ->
                    selectedSideModel = sm
                    tieAdvertSide.setText(selectedSideModel?.sideName)
                }
            }
        }
        tieAdvertBlock.setOnClickListener {
            if (blocksList.isNullOrEmpty()) {
                ParaNormalProcess.loadingProcessActivity(
                    context,
                    { UseCaseImpl().getBlocksList() }
                ) {
                    blocksList = it
                    selectBlock(context, blocksList){bm->
                        selectedBlockModel = bm
                        tieAdvertBlock.setText(selectedBlockModel?.blockName)
                    }
                }
            } else {
                selectBlock(context, blocksList){bm->
                    selectedBlockModel = bm
                    tieAdvertBlock.setText(selectedBlockModel?.blockName)
                }
            }
        }
        btnFilter.setOnClickListener {
            dialog.dismiss()
            callBack.onFilter(selectedSortModel,selectedBlockModel,selectedSideModel)
        }
        tvClearFilter.setOnClickListener {
            dialog.dismiss()
            callBack.onClearFilter()
        }
        return dialog
    }


    private fun selectBlock(
        context: BaseActivity,
        blocksList: List<BlocksModel>?,
        callback: (value: BlocksModel) -> Unit
    ) {
        blocksList?.let {
            SingleChoiceDialog(
                context,
                context.getString(R.string.advert_block),
                it,
                object : OnSingleDialogSelected<BlocksModel> {
                    override fun onSelected(value: BlocksModel) {
                        callback.invoke(value)
                    }
                }
            )
        }

    }
    private fun selectSides(
        context: BaseActivity,
        sidesList: List<SidesModel>?,
        callback: (value: SidesModel) -> Unit
    ) {
        sidesList?.let {
            SingleChoiceDialog(
                context,
                context.getString(R.string.advert_side),
                it,
                object : OnSingleDialogSelected<SidesModel> {
                    override fun onSelected(value: SidesModel) {
                        callback.invoke(value)
                    }
                }
            )
        }
    }



}