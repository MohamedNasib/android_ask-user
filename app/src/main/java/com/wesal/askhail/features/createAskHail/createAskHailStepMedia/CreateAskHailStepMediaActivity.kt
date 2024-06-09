package com.wesal.askhail.features.createAskHail.createAskHailStepMedia

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.FilePaths
import com.wesal.askhail.core.utilities.GetContentForImagesAndVideo
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAskHailStepMediaBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.askhail.features.createAskHail.createAskHailStepDetails.CreateAskHailStepDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvMediaModel

class CreateAskHailStepMediaActivity : BaseActivity() {
    lateinit var binding: ActivityCreateAskHailStepMediaBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var askId: Int = -1
    private var isEdit: Boolean = false
    private var selectedCoverImage: String? = null
    private var myTitle: String? = null
    private var description: String? = null
    private var show_name_status: String? = null
    private val SELECT_IMAGE_REQUEST = 1


    override fun layoutResource(): Int = R.layout.activity_create_ask_hail_step_media
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAskHailStepMediaBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        askId = intent.getIntExtra(ExtraConst.EXTRA_ASK_ID, -1)
        selectedCoverImage = intent.getStringExtra(ExtraConst.EXTRA_IMAGE)
        myTitle = intent.getStringExtra("title")
        description = intent.getStringExtra("description")
        show_name_status = intent.getStringExtra("show_name_status")


        if (isEdit) {
            setToolbar(getString(R.string.change_question_image))
            tool_binding.tvSteps.text = "   "
            binding.tvNoImage.gone()
            binding.tvStatusss.setText(R.string.edit_image_or_paint)
            binding.btnConfirmMedia.setText(R.string.save_changes)
            if (!selectedCoverImage.isNullOrEmpty()) {
                binding.ivCoverHolder.gone()
                binding.viewCover.visible()
                binding.ivCoverImage.loadServerImage(selectedCoverImage)
            }
        } else {
            setToolbar(getString(R.string.create_new_ask))
            tool_binding.tvSteps.text = "2/3"
        }

        clickers()
    }


    private fun clickers() {
        binding.ivCoverHolder.setOnClickListener {
//            storagePermissionForCoverMedia.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            pickerCoverImage.launch("image/*")

        }
        binding.viewCover   .setOnClickListener {
            binding.ivCoverHolder.visible()
            binding.viewCover.gone()
            selectedCoverImage = null

        }
        binding.btnConfirmMedia.setOnClickListener {
            if (isEdit) {
                editRequest()
            } else {
                performRequest()
            }
        }
        binding.tvNoImage.setOnClickListener { performRequest() }
    }

    private fun editRequest() {
        val isDelete = selectedCoverImage.isNullOrEmpty()
        ParaNormalProcess.loadingProcessActivity(
            this,
            {
                UseCaseImpl().editingQuestion(
                    askId,
                    selectedCoverImage,
                    myTitle,
                    description,
                    show_name_status,
                    isDelete
                )
            }
        ) {
            toasting(R.string.changes_saved, true)
            onBackPressed()
        }

    }

    private fun performRequest() {
        sStartActivity<CreateAskHailStepDetailsActivity>(
            ExtraConst.EXTRA_IMAGE to selectedCoverImage
        )
    }

    private val pickerCoverImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val absolutePath =
                FilePaths.copyFileToPath(this@CreateAskHailStepMediaActivity, it)?.absolutePath
            absolutePath?.let { q ->
                selectedCoverImage = q
                binding.ivCoverHolder.gone()
                binding.viewCover.visible()
                binding.ivCoverImage.loadServerImage(it)
            }
        }
    }
    private val storagePermissionForCoverMedia =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickerCoverImage.launch("image/*")
            }
        }



}