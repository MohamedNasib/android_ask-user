package com.wesal.askhail.core.extentions

 import java.text.SimpleDateFormat
import java.util.*

fun String.isYes(): Boolean {
    return this == "yes"
}

fun String.getDayWeekName(): String {
    val formatter = "yyy-MM-dd"
    val df = SimpleDateFormat(formatter, Locale.US)
    val date = df.parse(this)
    val outFormat = SimpleDateFormat("EE",Locale.US)
    val goal = outFormat.format(date)
    return goal
}
fun String.convertNormalToDate(): Date? {
    val formatter = "yyy-MM-dd"
    val df = SimpleDateFormat(formatter, Locale.US)
    return df.parse(this)
}