package com.wesal.askhail.features.pickLocation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.toAddress
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.GpsRequester
import com.wesal.askhail.core.utilities.LocationLiveData
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import kotlinx.coroutines.launch

class PickLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    lateinit var binding: ActivityPickLocationBinding

    private  var selectedLocation: LatLng?=null
    private lateinit var mMap: GoogleMap
    override fun layoutResource(): Int = R.layout.activity_pick_location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        hideStatusBar()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.fabNext.setOnClickListener {
            if (selectedLocation!=null){
                val intent = Intent()
                intent.putExtra(ExtraConst.EXTRA_LAT,selectedLocation!!.latitude)
                intent.putExtra(ExtraConst.EXTRA_LNG,selectedLocation!!.longitude)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GpsRequester(this@PickLocationActivity)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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
        mMap.setOnMarkerClickListener {
            false
        }
        mMap.isMyLocationEnabled = true
        LocationLiveData(this@PickLocationActivity, false)
            .observe(this@PickLocationActivity) { location ->
                if (location != null) {
                    val sydney = LatLng(location.getLatitude(), location.getLongitude())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    mMap.uiSettings.isMapToolbarEnabled = false
                    val cameraPosition =
                        CameraPosition.Builder().target(sydney).zoom(15f).build()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }
        initViews()
        mMap.setOnMapClickListener(this@PickLocationActivity)
    }

    private fun initViews() {
        binding.viewInit.visible()
        binding.viewControllers.gone()
    }

    override fun onMapClick(p0: LatLng) {
        p0.let {
            mMap.clear()
            val markerOptions = MarkerOptions()
            markerOptions.position(it)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(it));
            mMap.addMarker(markerOptions)?.setIcon( BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
            selectedLocation=it
            scopeIO.launch {
                val toAddress = it.toAddress(this@PickLocationActivity)
                scopeMain.launch {
                    binding.viewInit.gone()
                    binding.viewControllers.visible()
                    binding.tvAddress.text = toAddress
                }
            }

        }

    }
}