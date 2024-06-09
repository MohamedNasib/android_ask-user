package com.wesal.askhail.core.extentions

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception
import java.util.*


fun LatLng.toAddress(context: Context): String {
    try {

        val addresses: List<Address?>?
        val language = "ar"
        val geocode = Geocoder(context, Locale(language))
        addresses = geocode.getFromLocation(
            latitude,
            longitude,
            1
        ); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        var retAddress = ""
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            address?.let {
                retAddress = it.getAddressLine(0) + " " + it.locality
                retAddress = it.getAddressLine(0)
            }
        }
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        return retAddress

    } catch (e: Exception) {
        return " "
    }
}