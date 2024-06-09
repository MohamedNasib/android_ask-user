package com.wesal.askhail.features.subSectionsItems

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.invisible
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.value
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivitySubSectionItemsBinding
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListFragment
import com.wesal.askhail.features.subSectionsItems.ordersList.OrdersListFragment
import com.wesal.entities.models.SectionModel

class SubSectionItemsActivity : BaseActivity() {
    lateinit var binding: ActivitySubSectionItemsBinding
    private var isBusinessSection: Boolean = false
    private lateinit var sectionModel: SectionModel
    override fun layoutResource(): Int = R.layout.activity_sub_section_items
    private lateinit var fragmentList :MutableList<Fragment>;
    private val advertFragment = AdvertsListFragment()
    private val orderFragment = OrdersListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubSectionItemsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        sectionModel = intent.getParcelableExtra(ExtraConst.EXTRA_SECTION)!!
        isBusinessSection = intent.getBooleanExtra(ExtraConst.EXTRA_IS_BUSINESS, false)
        setToolbar(sectionModel.sectionName)
        fragmentList = mutableListOf()
        fragmentList.add(advertFragment)
        fragmentList.add(orderFragment)
        setUpTabsWithViewPager()
        clickers()
        binding.vpTypes.isUserInputEnabled=false
    }

    private fun clickers() {
        binding.ivFilter.setOnClickListener {
            advertFragment.whenFilterClicked()
        }
        binding.ivSearch.setOnClickListener {
            binding.viewTheToolBar.gone()
            binding.viewSearchBox.visible()
        }
        binding.ivCloseSearch.setOnClickListener {
            binding.viewTheToolBar.visible()
            binding.viewSearchBox.gone()
            advertFragment.searchWithText(null)
            binding.etSearch.setText("")
            setToolbar(sectionModel.sectionName)
        }
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etSearch.value().isNotEmpty()) {
                    val value = binding.etSearch.value()
                   setToolbar(value)
                    advertFragment.searchWithText(value)
                    binding.viewTheToolBar.visible()
                    binding.viewSearchBox.gone()
                }
                return@OnEditorActionListener false
            }
            false
        })
    }

    private fun setUpTabsWithViewPager() {
        val list = mutableListOf<String>()
        list.add(getString(R.string.adverts))
//        if (!isBusinessSection)
//            list.add(getString(R.string.orders))
        val adapter = TypesFragmentAdapter(this@SubSectionItemsActivity,list,fragmentList,sectionModel.sectionId,isBusinessSection)
        binding.vpTypes?.adapter =adapter
        TabLayoutMediator(binding.tabTypes,binding.vpTypes,false){tab,position->
            tab.text = list[position]
        }.attach()
        if (isBusinessSection){
            binding.tabTypes.gone()
        }
        binding.vpTypes.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position != 0){
                    binding.ivFilter.invisible()
                    binding.ivSearch.invisible()
                }else{
                    binding.ivFilter.visible()
                    binding.ivSearch.visible()
                }
            }
        })

    }
}