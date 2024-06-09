package com.wesal.askhail.features.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.HailPolicyActivity
import com.wesal.askhail.R
import com.wesal.askhail.core.extentions.requiredLoginArea
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.databinding.FragmentMoreBinding
import com.wesal.askhail.features.aboutApp.AboutAppActivity
import com.wesal.askhail.features.confimPhone.savemember.SaveMember
import com.wesal.askhail.features.contactUs.ContactUsActivity
import com.wesal.askhail.features.more.fixedPageDetails.FixedPageDetailsActivity
import com.wesal.askhail.features.myAccount.MyAccountActivity
import com.wesal.askhail.features.services.ServicesActivity
import com.wesal.askhail.features.settings.SettingsActivity
import com.wesal.askhail.features.snapShotRequest.SnapShotRequestActivity
import com.wesal.askhail.features.workWithUs.WorkWithUsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.FixedPageModel

class MoreFragment : RoundedBottomSheetDialogFragment(), FixedPagesAdapter.OnFixedPage {
    private var _binding: FragmentMoreBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UseCaseImpl().loadFixedPages()?.let {
            binding.rvPages.layoutManager = LinearLayoutManager(requireContext())
            val filter = it.toMutableList().filter { q ->
                q.fixedPageSlug != "real-estate-app"
            }
            val adapter = FixedPagesAdapter(filter.toMutableList(), this@MoreFragment)
            binding.rvPages.adapter = adapter
        }
        clickers()
    }

    private fun clickers() {
        binding.viewMyAccount.setOnClickListener {
            requiredLoginArea(binding.viewMyAccount, true) {
                context?.sStartActivity<MyAccountActivity>()
            }
        }
        binding.viewContactUs.setOnClickListener { context?.sStartActivity<ContactUsActivity>() }
        binding.viewServices.setOnClickListener { context?.sStartActivity<ServicesActivity>() }
        binding.viewSettings.setOnClickListener { context?.sStartActivity<SettingsActivity>() }
        binding.viewWorkWithUs.setOnClickListener { context?.sStartActivity<WorkWithUsActivity>() }
        binding.viewAskForShot.setOnClickListener { context?.sStartActivity<SnapShotRequestActivity>() }
        binding.viewShareApp.setOnClickListener { shareApp() }
        binding.viewAboutApp.setOnClickListener { context?.sStartActivity<AboutAppActivity>() }
        binding.hailPolicy.setOnClickListener {context?.sStartActivity<HailPolicyActivity>()}
        binding.saveMember.setOnClickListener {context?.sStartActivity<SaveMember>()}
    }

    private fun shareApp() {
        IntentsForActions.sharePhotoWithText(
            requireActivity(),
            null,
            getString(R.string.share_app)
        )
    }

    override fun onFixedPage(model: FixedPageModel) {
        context?.sStartActivity<FixedPageDetailsActivity>(
            ExtraConst.EXTRA_FIXED_PAGE_SLUG to model.fixedPageSlug,
            ExtraConst.EXTRA_FIXED_PAGE_TITLE to model.fixedPageTitle
        )
    }
}