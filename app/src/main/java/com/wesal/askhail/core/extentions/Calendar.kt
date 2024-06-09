package com.wesal.askhail.core.extentions

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.toYyyyMmDdString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return format.format(time)
}