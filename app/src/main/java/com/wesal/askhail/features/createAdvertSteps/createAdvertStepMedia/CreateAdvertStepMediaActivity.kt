package com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia

import android.Manifest
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
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
import com.wesal.askhail.databinding.ActivityCreateAdvertStepMediaBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.advertDetails.EditMediaModel
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvMediaModel

class CreateAdvertStepMediaActivity : BaseActivity(), ImagesPickerAdapter.OnImagesClicked {





    lateinit var binding: ActivityCreateAdvertStepMediaBinding;
    lateinit var tool_binding: MyToolbarStepsBinding
    private var editModel: EditMediaModel? = null
    private var isEdit: Boolean = false
    private var selectedCoverImage: String? = null
    private var advertId: Int = -1
    private var deletedImagesIds = mutableListOf<Int>()
    override fun layoutResource(): Int = R.layout.activity_create_advert_step_media
    val adapter: ImagesPickerAdapter = ImagesPickerAdapter(mutableListOf(), this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertStepMediaBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID, -1);
        isEdit = intent.getBooleanExtra(ExtraConst.EXTRA_IS_EDIT, false)
        editModel = intent.getParcelableExtra(ExtraConst.EXTRA_MODEL)
        hideStatusBar()
        setUpView()
        if (isEdit) {
            setToolbar(getString(R.string.edit_advert_media))
            tool_binding.tvSteps.text = "  "
            binding.btnConfirmMedia.setText(R.string.save_changes)
            updateEditInfo()
        } else {
            setToolbar(getString(R.string.create_new_advert))
            tool_binding.tvSteps.text = "4/6"
        }

        clickers()
    }

    private fun updateEditInfo() {
        editModel?.let {
            adapter.addMoreInList(it.media)
            binding.rvMedia.visible()
            binding.viewMediaHolder.gone()
            if (!it.promoImage.isNullOrEmpty() && !it.promoImage.contains("no_image")) {
                binding.ivCoverHolder.gone()
                binding.viewCover.visible()
                binding.ivCoverImage.loadServerImage(it.promoImage)
                selectedCoverImage = it.promoImage
            }
        }

    }

    private fun setUpView() {
        binding.rvMedia.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMedia.adapter = adapter
    }

    private fun clickers() {
        binding.viewMediaHolder.setOnClickListener { showPicker() }
        binding.tvAddMedia.setOnClickListener { showPicker() }
        binding.ivCoverHolder.setOnClickListener {
            pickerCoverImage.launch("image/*")
        //            storagePermissionForCoverMedia.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.viewCover.setOnClickListener {
            binding.ivCoverHolder.visible()
            binding.viewCover.gone()
            selectedCoverImage = null
        }
        binding.btnConfirmMedia.setOnClickListener {
            if (validateRequest()) {
                if (isEdit) {
                    editRequest()

                } else {
                    performRequest()

                }
            }
        }
    }

    private fun performRequest() {
        if (selectedCoverImage.isNullOrEmpty()) {
//            toasting(R.string.imgs_first)
            sStartActivity<CreateAdvertStepSpecificationActivity>(
                ExtraConst.EXTRA_ADVERT_ID to advertId
            )
        } else {
            ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().createAdvertStepMedia(advertId, selectedCoverImage, adapter.list) }
            ) {
                sStartActivity<CreateAdvertStepSpecificationActivity>(
                    ExtraConst.EXTRA_ADVERT_ID to advertId
                )
            }
        }
    }

    private fun editRequest() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            {
                UseCaseImpl().updateAdvertStepMedia(
                    advertId,
                    selectedCoverImage,
                    adapter.list,
                    deletedImagesIds
                )
            }
        ) {
            toasting(R.string.changes_saved, true)
            onBackPressed()
        }
    }

    private fun validateRequest(): Boolean {

//        if (adapter.itemCount > 3) {
//            toasting("الحد الاقصي 2")
//            return false
//        }
        return true
    }

    private val pickerMediaImage = registerForActivityResult(GetContentForImagesAndVideo()) {
        if (it != null) {
            val absolutePath =
                FilePaths.copyFileToPath(this@CreateAdvertStepMediaActivity, it)?.absolutePath
            absolutePath?.let { q ->
                val uriImage = isUriImage(q)
                if (uriImage) {
                    adapter.addMoreInList(
                        listOf(
                            AdvMediaModel(-1, -1, null, q, 0)
                        )
                    )
                    binding.rvMedia.visible()
                    binding.viewMediaHolder.gone()
                } else {
                    if (adapter.canAddMoreVideos()) {
                        val fileThumb =
                            IntentsForActions.getFileThumb(this@CreateAdvertStepMediaActivity, q)
                        adapter.addMoreInList(
                            listOf(
                                AdvMediaModel(
                                    -1,
                                    -1,
                                    q,
                                    fileThumb.absolutePath,
                                    getVideoDuration(it)
                                )
                            )
                        )
                        binding.rvMedia.visible()
                        binding.viewMediaHolder.gone()
                    } else {
                        toasting(R.string.max_video)
                    }
                }

            }
        }
    }

    private fun getVideoDuration(it: Uri?): Int {
        if (it != null) {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(this@CreateAdvertStepMediaActivity, it)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillisec = time!!.toLong()
            retriever.release()
            return (timeInMillisec / 1000).toInt()
        }
        return 0
    }

    private val storagePermissionForImageMedia =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickerMediaImage.launch("video/*, image/*")
            }
        }
    private val pickerCoverImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val absolutePath =
                FilePaths.copyFileToPath(this@CreateAdvertStepMediaActivity, it)?.absolutePath
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
                try {
                    pickerCoverImage.launch("image/*")
                }catch (e : Exception){e.printStackTrace()}
            }
        }

    private fun showPicker() {
//        storagePermissionForImageMedia.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        pickerMediaImage.launch("video/*, image/*")

    }

    override fun onImagesClicked(model: AdvMediaModel) {
        if (adapter.itemCount == 0) {
            binding.rvMedia.gone()
            binding.viewMediaHolder.visible()
        }
        if (model.mediaId != -1) {
            deletedImagesIds.add(model.mediaId)
        }
    }
}