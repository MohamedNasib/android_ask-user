package com.wesal.askhail.features.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivityServicesBinding

class ServicesActivity : BaseActivity() {
    lateinit var binding: ActivityServicesBinding
    override fun layoutResource(): Int =R.layout.activity_services
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.services))
        setUpTabsWithViewPager()
    }

    private fun setUpTabsWithViewPager() {
    // my tabs
        val list = mutableListOf<String>()
        list.add(getString(R.string.contrato))
        list.add(getString(R.string.rent_contract))
        list.add(getString(R.string.others))
        val adapter = ServicesFragmentAdapter(this@ServicesActivity,list)
        binding.vpTypes?.adapter =adapter
        TabLayoutMediator(binding.tabTypes,binding.vpTypes,false){tab,position->
            tab.text = list[position]
        }.attach()
    }
}