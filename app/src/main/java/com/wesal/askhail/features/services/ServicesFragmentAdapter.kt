package com.wesal.askhail.features.services

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.features.services.servicesList.ServicesListFragment
import com.wesal.askhail.features.subSectionsItems.advertsList.AdvertsListFragment
import com.wesal.askhail.features.subSectionsItems.ordersList.OrdersListFragment

class ServicesFragmentAdapter(
    fragment: FragmentActivity,
    private val list: MutableList<String>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment {
        val serviceType = when (position) {
            0 -> {
                "contracts"
            }
            1 -> {
                "rental_contracts"
            }
            2 -> {
                "other"
            }
            else -> ""
        }

        val fragment = ServicesListFragment()
        fragment.arguments = bundleOf(
            ExtraConst.EXTRA_SERVICE_TYPE to serviceType
        )
        return fragment

    }

}
