package com.wesal.domain.gateways.cacheGateway

import com.wesal.entities.models.CountryModel
import com.wesal.entities.models.FixedPageModel

val cacheGateway by lazy { CacheGateWay() }

class CacheGateWay : ICacheGateWay {
    private object Con {
        const val COUNTRY = "A"
        const val FIXED_PAGES = "B"

    }

    override fun saveCounties(value: List<CountryModel>?) {
        value?.let {
            CacheHelper.save(Con.COUNTRY, it)
        }
    }

    override fun loadCountryList(): List<CountryModel>? {
        return CacheHelper.load(Con.COUNTRY)
    }

    override fun saveFixedPages(value: List<FixedPageModel>?) {
        value?.let {
            CacheHelper.save(Con.FIXED_PAGES, it)
        }
    }

    override fun loadFixedPages(): List<FixedPageModel>? {
        return CacheHelper.load(Con.FIXED_PAGES)
    }
}