package com.example.tracksleep

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.tracksleep.database.SleepNight
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


fun convertNumericalQualityToString(quality :Int, resources: Resources) : String{
    var qualityString = ""
    when(quality){
        -1 -> "--"
        0 -> qualityString =  resources.getString(R.string.ver_bad)
        1 -> qualityString = resources.getString(R.string.poor)
        2 -> qualityString = resources.getString(R.string.ok)
        3 -> qualityString = resources.getString(R.string.good)
        4 -> qualityString = resources.getString(R.string.great)
        5 -> qualityString = resources.getString(R.string.excellent)
    }
    return qualityString
}
/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */

fun convertLongToDateString(systemTime : Long) : String{
    return SimpleDateFormat("EEE MMM-dd-yyyy 'time:' HH:mm ")
            .format(systemTime).toString()
}

fun formatNights(nights : List<SleepNight>, resources : Resources) : Spanned{
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        nights.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append("\t${convertLongToDateString(it.startTimeMilli)} <br>")
            if (it.endTimeMilli != it.startTimeMilli){
                append(resources.getString(R.string.end_time))
                append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
                append(resources.getString(R.string.quality))
                append("\t ${convertNumericalQualityToString(it.sleepQuality, resources)} <br>")

                append(resources.getString(R.string.hours_slept))
                // Hours
                append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
                // Seconds
                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")

            }
        }
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

}