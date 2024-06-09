package com.wesal.askhail.features.home

import android.gesture.GestureOverlayView
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.extentions.requiredLoginArea
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.IntentsForActions
import com.wesal.askhail.databinding.FragmentHomeBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.inactiveAdvertList.InActiveAdvertsAdapter
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.specialAdvertsList.SpecialAdvertsListActivity
import com.wesal.askhail.features.subSections.SubSectionAdapter
import com.wesal.askhail.features.subSections.SubSectionAdapterCircle
import com.wesal.askhail.features.subSections.SubSectionsActivity
import com.wesal.askhail.features.subSectionsItems.SubSectionItemsActivity
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListAdapter2
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.base.PaginationModel
import com.wesal.entities.models.AboutAppModel
import com.wesal.entities.models.AdvertModel
import com.wesal.entities.models.HomeModel
import com.wesal.entities.models.SectionModel
import com.wesal.entities.models.SpecialAdvertisementModel
import kotlinx.coroutines.launch
import java.util.HashMap

class HomeFragment : BaseFragment(), HomeSpecialAdvertAdapter.OnSpecialAdvert,
    HomeRealStateAdapter.OnRealStateSection, HomeBusinessAdapter.OnBusinessSection,
    SubSectionAdapterCircle.OnSubSectionClicked, AdvertsListAdapter2.OnAdvertCallBack {
    private var model: HomeModel? = null
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun layoutRes(): Int = R.layout.fragment_home
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launching()

        // Set a delay to ensure the TextView is fully loaded
        Handler().postDelayed({
            binding.marqueeLabel.isSelected = true
        }, 1000) // Delay of 1 second
    }

    private fun launching() {
        ParaNormalProcess.viewProcessFragment(
            this,
            { UseCaseImpl().getHomeData() }
        ) {
            model = it
            scopeMain.launch {
//                delay(1000)
                setUpView()

            }
        }

        ParaNormalProcess.viewProcessFragment(
            this,
            { UseCaseImpl().getAboutApp() }
        ) {
            updateUi(it)
        }

        ParaNormalProcess.viewProcessFragment(
            this,
            {UseCaseImpl().getMainCategories()}
        ){
            it?.let { it1 -> updateUi(it1) }
        }

        ParaNormalProcess.viewProcessFragment(
            this,
            {UseCaseImpl().getAdvertsInSubSection(0, 1, hashMapOf<String,Any>())}
        ){
            it?.let { it1 -> updateUi2(it1) }
        }
    }

    private fun updateUi2(it: PaginationModel<AdvertModel>) {
        val advertList: MutableList<AdvertModel?> = mutableListOf()
        for (advert in it.data) {
            advertList.add(advert)
        }

        val adapter = AdvertsListAdapter2(advertList, false, this)
        binding.vpLatestSlider.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        binding.vpLatestSlider.adapter=adapter
    }

    private fun updateUi(it: List<SectionModel>) {
        val adapter = SubSectionAdapterCircle(it.toMutableList(),this)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter=adapter
    }

    private fun setUpView() {
        ///////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////  SetUp Slider Special Advert  ////////////////////////////////
        ////////////////////////  SetUp Slider Special Advert  ////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////
        val specialList = mutableListOf<SpecialAdvertisementModel?>()
        specialList.addAll(model!!.specialAdvertisements)
        specialList.add(null)
        binding.vpSlider?.adapter = HomeSpecialAdvertAdapter(specialList, this@HomeFragment)

        binding.vpSlider?.let {
            with(it) {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
            }
        }
        context?.let {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
            binding.vpSlider?.setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == GestureOverlayView.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        if (position == 0F) {

                        } else {
                            page.translationX = -offset
                        }
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////  SetUp Slider RealStates  ////////////////////////////////
        ////////////////////////  SetUp Slider RealStates  ////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////
        binding.rvHomeRealState?.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        val realStateAdapter = HomeRealStateAdapter(model!!.realEstate.toMutableList(), this)
        binding.rvHomeRealState?.adapter = realStateAdapter
        ///////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////  SetUp Slider RealStates  ////////////////////////////////
        ////////////////////////  SetUp Slider RealStates  ////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////
        val gridLayout = GridLayoutManager(activity, 2)
        binding.rvHomeBusiness?.layoutManager = gridLayout
        val homeBusinessAdapter = HomeBusinessAdapter(model!!.business.toMutableList(), this)
        binding.rvHomeBusiness?.adapter = homeBusinessAdapter
        gridLayout.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 2
                    else -> 1
                }
            }
        }
    }

    override fun onToggleFavorite(model: SpecialAdvertisementModel) {
        requiredLoginArea(binding.rvHomeRealState, true) {
            ParaNormalProcess.silentProcessFragment(
                this,
                { UseCaseImpl().toggleFavoritesAdvert(model.advId) }
            ) {
                requireActivity().toasting("onToggleFavorite")
            }
        }
    }

    override fun onViewAdvertDetails(model: SpecialAdvertisementModel) {
        context?.sStartActivity<AdvertDetailsActivity>(
            ExtraConst.EXTRA_ADVERT_ID to model.advId
        )
    }

    override fun onSeeAllSpecialAdverts() {
        context?.sStartActivity<SpecialAdvertsListActivity>()

    }

    override fun onRealStateSection(model: SectionModel) {
        requireActivity().sStartActivity<SubSectionsActivity>(
            ExtraConst.EXTRA_SECTION to model,
            ExtraConst.EXTRA_IS_BUSINESS to false
        )
    }

    override fun onBusinessSection(model: SectionModel) {
        requireActivity().sStartActivity<SubSectionsActivity>(
            ExtraConst.EXTRA_SECTION to model,
            ExtraConst.EXTRA_IS_BUSINESS to true
        )

    }

    private fun updateUi(model: AboutAppModel?) {
        binding.marqueeLabel.text = model?.appMarquee
        model?.let {
            binding.viewTwitter.setOnClickListener { _ ->
                activity?.let { it1 ->
                    IntentsForActions.twitter(
                        it1,
                        it.appTwitter
                    )
                }
            }
            binding.imgAskFor.setOnClickListener { _ ->
                activity?.let { it1 ->
                    (activity as MainActivity).navController.navigate(R.id.askHailFragment)
                }
            }
            binding.viewSnap.setOnClickListener { _ ->
                activity?.let { it1 ->
                    IntentsForActions.snapChat(
                        it1,
                        it.appSnapchat
                    )
                }
            }
            binding.viewInstgram.setOnClickListener { _ ->
                activity?.let { it1 ->
                    IntentsForActions.instgram(
                        it1,
                        it.appInstagram
                    )
                }
            }
            binding.viewWhatsApp.setOnClickListener { _ ->
                activity?.let { it1 ->
                    IntentsForActions.whatsApp(
                        it1,
                        it.appWhatsapp
                    )
                }
            }
            binding.viewTikTok.setOnClickListener { _ ->
                activity?.let { it1 ->
                    IntentsForActions.tiktok(
                        it1,
                        it.appTikTok
                    )
                }
            }
        }
    }

    override fun onSubSectionClicked(model: SectionModel) {
        requireActivity().sStartActivity<SubSectionsActivity>(
            ExtraConst.EXTRA_SECTION to model,
            ExtraConst.EXTRA_IS_BUSINESS to false
        )
    }

    override fun onViewAdvertDetails(model: AdvertModel) {
        context?.sStartActivity<AdvertDetailsActivity>(
            ExtraConst.EXTRA_ADVERT_ID to model.advId
        )
    }

    override fun onToggleFavorite(model: AdvertModel) {
        ParaNormalProcess.silentProcessFragment(
            this,
            { UseCaseImpl().toggleFavoritesAdvert(model.advId) }
        ) {
        }
    }
}