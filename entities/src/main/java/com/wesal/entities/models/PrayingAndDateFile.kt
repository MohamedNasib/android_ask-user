package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class PrayingContainerModelModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val theData: PrayingTimeModel
)

data class PrayingTimeModel(
    @SerializedName("timings")
    val timings: TimingsModel,
    @SerializedName("date")
    val date: DateModel,
    @SerializedName("meta")
    val meta: MetaModel
)

data class TimingsModel(
    @SerializedName("Fajr")
    val fajr: String,
    @SerializedName("Sunrise")
    val sunrise: String,
    @SerializedName("Dhuhr")
    val dhuhr: String,
    @SerializedName("Asr")
    val asr: String,
    @SerializedName("Sunset")
    val sunset: String,
    @SerializedName("Maghrib")
    val maghrib: String,
    @SerializedName("Isha")
    val isha: String,
    @SerializedName("Imsak")
    val imsak: String,
    @SerializedName("Midnight")
    val midnight: String
)

data class DateModel(
    @SerializedName("readable")
    val readable: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("hijri")
    val hijri: HijriModel,
    @SerializedName("gregorian")
    val gregorian: GregorianModel
)

data class MetaModel(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("method")
    val method: MethodModel,
    @SerializedName("latitudeAdjustmentMethod")
    val latitudeAdjustmentMethod: String,
    @SerializedName("midnightMode")
    val midnightMode: String,
    @SerializedName("school")
    val school: String,
    @SerializedName("offset")
    val offset: OffsetModel
)

data class HijriModel(
    @SerializedName("date")
    val date: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("weekday")
    val weekday: WeekdayModel,
    @SerializedName("month")
    val month: MonthModel,
    @SerializedName("year")
    val year: String,
    @SerializedName("designation")
    val designation: DesignationModel,
    @SerializedName("holidays")
    val holidays: List<Any>
)

data class GregorianModel(
    @SerializedName("date")
    val date: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("weekday")
    val weekday: WeekdayModelX,
    @SerializedName("month")
    val month: MonthModelX,
    @SerializedName("year")
    val year: String,
    @SerializedName("designation")
    val designation: DesignationModelX
)

data class WeekdayModel(
    @SerializedName("en")
    val en: String,
    @SerializedName("ar")
    val ar: String
)

data class MonthModel(
    @SerializedName("number")
    val number: Int,
    @SerializedName("en")
    val en: String,
    @SerializedName("ar")
    val ar: String
)

data class DesignationModel(
    @SerializedName("abbreviated")
    val abbreviated: String,
    @SerializedName("expanded")
    val expanded: String
)

data class WeekdayModelX(
    @SerializedName("en")
    val en: String
)

data class MonthModelX(
    @SerializedName("number")
    val number: Int,
    @SerializedName("en")
    val en: String
)

data class DesignationModelX(
    @SerializedName("abbreviated")
    val abbreviated: String,
    @SerializedName("expanded")
    val expanded: String
)

data class MethodModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("params")
    val params: ParamsModel
)

data class OffsetModel(
    @SerializedName("Imsak")
    val imsak: Int,
    @SerializedName("Fajr")
    val fajr: Int,
    @SerializedName("Sunrise")
    val sunrise: Int,
    @SerializedName("Dhuhr")
    val dhuhr: Int,
    @SerializedName("Asr")
    val asr: Int,
    @SerializedName("Maghrib")
    val maghrib: Int,
    @SerializedName("Sunset")
    val sunset: Int,
    @SerializedName("Isha")
    val isha: Int,
    @SerializedName("Midnight")
    val midnight: Int
)

data class ParamsModel(
    @SerializedName("Fajr")
    val fajr: Any,
    @SerializedName("Isha")
    val isha: Any
)