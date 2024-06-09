package com.wesal.askhail.features.subSectionsItems

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListFragment
import com.wesal.askhail.features.subSectionsItems.ordersList.OrdersListFragment

class TypesFragmentAdapter(
    fragment: FragmentActivity,
    private val list: MutableList<String>,
    private val fragmentList: MutableList<Fragment>,
    private val subSectionId: Int,
    private val businessSection: Boolean
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =list.size
    override fun createFragment(position: Int): Fragment {
        if (position == 0){
            val fragment =fragmentList[0]
            fragment.arguments = bundleOf(ExtraConst.EXTRA_SECTION_ID to subSectionId,ExtraConst.EXTRA_IS_BUSINESS to businessSection)
            return fragment
        }else{
            val fragment = fragmentList[1]
            fragment.arguments = bundleOf(ExtraConst.EXTRA_SECTION_ID to subSectionId,ExtraConst.EXTRA_IS_BUSINESS to businessSection)
            return fragment
        }

    }

}
