package com.wesal.askhail.features.prayAndWeather

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.LocationLiveData
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivityPrayAndWeatherBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.pickLocation.PickLocationActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.CustomPrayingTimeModel
import com.wesal.entities.models.PrayingTimeModel
import com.wesal.entities.models.WeatherContainerModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class PrayAndWeatherActivity : BaseActivity() {
    lateinit var binding: ActivityPrayAndWeatherBinding
    override fun layoutResource(): Int = R.layout.activity_pray_and_weather
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrayAndWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.pray_time))
        launchingPraying()
        clickers()
    }

    private val requestPermissionLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(this, PickLocationActivity::class.java)
                resultLauncher.launch(intent)
            } else {

            }
        }
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    scopeIO.launch {
                        val lat = it.getDoubleExtra(ExtraConst.EXTRA_LAT, 0.0)
                        val lng = it.getDoubleExtra(ExtraConst.EXTRA_LNG, 0.0)
                        getPrayingData(lat, lng)
                        getWeatherData(lat, lng)
//                            val toAddress =
//                                    LatLng(lat, lng).toAddress(this@PrayAndWeatherActivity)
//                            scopeMain.launch {
//                                tieAdvertLocation.setText(toAddress)
//
//                            }
                    }
                }
            }
        }

    private fun clickers() {
        binding.ivLocation.setOnClickListener {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        binding.ivLocation.invisible()
    }

    private fun launchingPraying() {
        controlViews()
        val lastLocation = UseCaseImpl().getUserLastLocation()
        if (lastLocation.latitude == "0".toDouble()) {
            LocationLiveData(this@PrayAndWeatherActivity, false)
                .observe(this@PrayAndWeatherActivity) { location ->
                    if (location != null) {
                        Log.e("location", "==> 1 " + location.latitude)
                        getPrayingData(location.latitude, location.longitude)
                        getWeatherData(location.latitude, location.longitude)
                    }
                }
        } else {
            Log.e("location", "==> 2 " + lastLocation.latitude)
            getPrayingData(lastLocation.latitude, lastLocation.longitude)
            getWeatherData(lastLocation.latitude, lastLocation.longitude)
        }


    }


    private fun getPrayingData(latitude: Double, longitude: Double) {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getPrayingTime(latitude, longitude) }
        ) {
            it?.let {
                updatePrayingAndTime(it)

            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updatePrayingAndTime(it: PrayingTimeModel) {
        binding.tvDayM.text = "${it.date.gregorian.day}"
//        val dateG = it.date.gregorian.date
//        val webFormat = it.date.gregorian.format
//        val format = SimpleDateFormat(webFormat, Locale.US)
//        val date = format.parse(dateG)
//        val dateFormat = SimpleDateFormat("MMM yyyy", Locale(UseCaseImpl().getSystemLanguage()))
//        val dateTime = dateFormat.format(date)
        binding.tvDateM.text = "${it.date.gregorian.month.en} ${it.date.gregorian.year}"
        binding.tvDayH.text = "${it.date.hijri.day}"
        binding.tvDateH.text = "${it.date.hijri.month.ar} ${it.date.hijri.year} "
        try {
            val cal: Calendar = UmmalquraCalendar()
            binding.tvDayH.text = "${cal.get(Calendar.DAY_OF_MONTH)}"
            binding.tvDateH.text = "${
                cal.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    Locale(UseCaseImpl().getSystemLanguage())
                )
            } ${ String.format(Locale(UseCaseImpl().getSystemLanguage()),"%d", cal.get(Calendar.YEAR))}"

        } catch (e: Exception) {
            e.printStackTrace()
        }


        binding.rvPraying.layoutManager = LinearLayoutManager(this)
        val adapter = PrayingTimeAdapter(getPrayingList(it), null)
        binding.rvPraying.adapter = adapter
    }

    private fun getPrayingList(it: PrayingTimeModel): MutableList<CustomPrayingTimeModel?> {
        val list = mutableListOf<CustomPrayingTimeModel?>()
        list.add(
            CustomPrayingTimeModel(
                getString(R.string.pray_fajr),
                change24Hto12H(it.timings.fajr)
            )
        )
        list.add(
            CustomPrayingTimeModel(
                getString(R.string.pray_dhuhr),
                change24Hto12H(it.timings.dhuhr)
            )
        )
        list.add(
            CustomPrayingTimeModel(
                getString(R.string.pray_asr),
                change24Hto12H(it.timings.asr)
            )
        )
        list.add(
            CustomPrayingTimeModel(
                getString(R.string.pray_maghrib),
                change24Hto12H(it.timings.maghrib)
            )
        )
        list.add(
            CustomPrayingTimeModel(
                getString(R.string.pray_isha),
                change24Hto12H(it.timings.isha)
            )
        )
        return list
    }

    private fun change24Hto12H(originalString: String): String {
        val dateG = originalString
        val webFormat = "HH:mm"
        val format = SimpleDateFormat(webFormat, Locale.US)
        val date = format.parse(dateG)
        val dateFormat = SimpleDateFormat("hh:mm a", Locale(UseCaseImpl().getSystemLanguage()))
        if (date != null) {
            val dateTime = dateFormat.format(date)
            return dateTime
        } else {
            return originalString;
        }
    }

    private fun getWeatherData(latitude: Double, longitude: Double) {
        ParaNormalProcess.silentProcessActivity(
            this,
            { UseCaseImpl().getWeatherInformation(latitude, longitude) }
        ) { wcm ->
            wcm?.let {
                updateWeatherInfo(it)
            }

        }
    }

    private fun updateWeatherInfo(it: WeatherContainerModel) {
        binding.rvWeather.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = WeatherAdapter(it.daily.toMutableList(), null)
        binding.rvWeather.adapter = adapter
        binding.viewWeather.visible()
    }
}