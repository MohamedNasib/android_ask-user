package com.wesal.askhail.features.advertsOnMap

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.changeMapStyle
import com.wesal.askhail.core.extentions.loadServerImage
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.GapiGialog
import com.wesal.askhail.core.utilities.LocationLiveData
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertModel
import timber.log.Timber


class AdvertOnMapActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: ActivitySuccessPageBinding;

    private var sectionId: Int = -1
    private lateinit var mMap: GoogleMap
    override fun layoutResource(): Int =R.layout.activity_advert_on_map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setToolbar(getString(R.string.advert_on_map))
        sectionId=  intent.getIntExtra(ExtraConst.EXTRA_SECTION_ID,-1)
    }
    override fun onStart() {
        super.onStart()
        GapiGialog(this@AdvertOnMapActivity)
    }
    private fun setUpView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.changeMapStyle(this,R.raw.mapstyle)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap.isMyLocationEnabled = true

        LocationLiveData(this@AdvertOnMapActivity, false)
            .observe(this@AdvertOnMapActivity) { location ->
                if (location != null) {
                    val sydney = LatLng(location.getLatitude(), location.getLongitude())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    mMap.uiSettings.isMapToolbarEnabled = false
                    val cameraPosition =
                        CameraPosition.Builder().target(sydney).zoom(15f).build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }

        mMap.setOnMarkerClickListener {
            Timber.e("Marker Info ==> ${it.tag}")
            it.showInfoWindow()
            true
        }
        mMap.setOnInfoWindowClickListener {
            it.let {
                val model = it.tag as AdvertModel?
                model?.let {q->
                    sStartActivity<AdvertDetailsActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to  q.advId
                    )
                }
            }
        }
        val customInfoWindow = MyInfoWindowAdapter(this)
        mMap.setInfoWindowAdapter(customInfoWindow)
        startTrackingMapMoving()
    }
    private fun startTrackingMapMoving() {
        mMap.setOnCameraIdleListener {
            val midLatLng = mMap.cameraPosition.target
            Timber.e(" Location Changed to ${midLatLng.latitude} ${midLatLng.longitude}")
            loadMarkers(midLatLng.latitude,midLatLng.longitude)
        }
    }
    private fun loadMarkers(latitude: Double, longitude: Double) {
        ParaNormalProcess.silentProcessActivity(
            this@AdvertOnMapActivity,
            {UseCaseImpl().getAdvertOnMap(sectionId,latitude,longitude)}
//            {UseCaseImpl().getAdvertOnMap(sectionId,0.0, 0.0)}
        ){it->
            it?.let {q->
                putAdvertOnMap(q.data)
            }
        }
    }
    private fun putAdvertOnMap(data: List<AdvertModel>) {
        if (this::mMap.isInitialized){
            mMap.clear()
            for (advert in data){
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(advert.advLat.toDouble(),advert.advLng.toDouble()))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                val addMarker = mMap.addMarker(markerOptions)
                addMarker?.tag = advert
            }
        }
    }

    class MyInfoWindowAdapter(val activity: Activity): GoogleMap.InfoWindowAdapter {
        override fun getInfoContents(marker: Marker): View? {
            val view = LayoutInflater.from(activity).inflate(R.layout.view_marker_info,null)
            val ivMapMarker = view.findViewById<ImageView>(R.id.ivMapMarker)
            val tvAdvertName = view.findViewById<TextView>(R.id.tvAdvertName)
            marker?.let {
                val model = it.tag as AdvertModel?
                model?.let {q->
//                    ivMapMarker.loadServerImage(q.advPromotionalImage)
                    Picasso.get()
                        .load(q.advPromotionalImage)
//                        .placeholder( R.drawable.progress_animation)
                        .into(ivMapMarker,MarkerCallback(marker,q.advPromotionalImage,ivMapMarker))
                    tvAdvertName.text = q.advTitle
                }
            }
            return  view
        }

        override fun getInfoWindow(p0: Marker): View? {
            return  null
        }
    }
    class MarkerCallback internal constructor(
        marker: Marker?,
        URL: String,
        userPhoto: ImageView
    ) :
        Callback {
        var marker: Marker? = null
        var URL: String
        var userPhoto: ImageView
        override fun onSuccess() {
            if (marker != null && marker!!.isInfoWindowShown) {
                marker?.hideInfoWindow()
                Picasso.get()
                    .load(URL)
//                    .placeholder( R.drawable.progress_animation)
                    .into(userPhoto)

                marker?.showInfoWindow()
            }
        }

        override fun onError(e: Exception) {}

        init {
            this.marker = marker
            this.URL = URL
            this.userPhoto = userPhoto
        }
    }

}