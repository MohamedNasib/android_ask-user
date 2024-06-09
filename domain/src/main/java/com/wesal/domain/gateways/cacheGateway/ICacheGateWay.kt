package com.wesal.domain.gateways.cacheGateway

import com.wesal.entities.models.CountryModel
import com.wesal.entities.models.FixedPageModel


interface ICacheGateWay {
    fun saveCounties(value: List<CountryModel>?)
    fun loadCountryList(): List<CountryModel>?

    fun saveFixedPages(value:List<FixedPageModel>?)
    fun loadFixedPages():List<FixedPageModel>?
}