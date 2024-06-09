package com.wesal.askhail.core.extentions

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.wesal.askhail.R
import timber.log.Timber
import java.io.File

fun ImageView.loadServerImage(url: String?) {
    url?.let {
        Picasso.get()
            .load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerInside()
            .placeholder( R.drawable.progress_animation)
            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception) {
                    Picasso.get()
                        .load(url)
                        .placeholder( R.drawable.progress_animation)
                        .fit()
                        .centerInside()
                        .into(this@loadServerImage)
                }
            })
    }
}

fun ImageView.loadServerImage(url: Uri?) {
    url?.let {
        Picasso.get()
            .load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerInside()
            .placeholder( R.drawable.progress_animation)
            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception) {
                    Picasso.get()
                        .load(url)
                        .placeholder( R.drawable.progress_animation)
                        .fit()
                        .centerInside()
                        .into(this@loadServerImage)
                }
            })
    }
}

fun ImageView.loadServerImage(file: File?) {
    file?.let {
        Picasso.get()
            .load(file)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerInside()
            .placeholder( R.drawable.progress_animation)
            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception) {
                    Picasso.get()
                        .load(file)
                        .placeholder( R.drawable.progress_animation)
                        .fit()
                        .centerInside()
                        .into(this@loadServerImage)
                }
            })
    }
}

fun ImageView.loadServerImageForWeather(url: String?) {
    val finalResult = "https://openweathermap.org/img/wn/$url@2x.png"
    Log.e("finalResult", finalResult)
    url?.let {
        Picasso.get()
            .load(finalResult)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerInside()

            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception) {
                    Picasso.get()
                        .load(finalResult)

                        .fit()
                        .centerInside()
                        .into(this@loadServerImageForWeather)
                }
            })
    }
}

fun ImageView.loadMapImage(lat: String?, lng: String?) {
    val url =
        "https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=600x300&maptype=roadmap&markers=color:red%7Clabel:C%7C$lat,$lng&key=${this.context.getString(
            R.string.google_maps_key
        )}"
    Log.e("Url","==> "+url)
     url.let {
        Picasso.get()
            .load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .fit()
            .centerInside()
            .placeholder(R.drawable.ic_placeholder_map)
            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(e: Exception) {
                    Timber.e(e)
                    Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder_map)
                        .fit()
                        .centerInside()
                        .into(this@loadMapImage)
                }
            })
    }
}