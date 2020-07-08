package com.example.inspector.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucas Alves dos Santos on 07/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
data class Control(
    var id: String = "",
    val date: Date? = null,
    var placeName: String = "",
    var inspectedName: String = "",
    var workLoad: String = "",
    var usageFactors: String = "",
    var occupation: String = "",
    var primaryAttenuation: String = "",
    var integrity: String = "",
    var assessmentPrimary: String = "",
    var assessmentSecond: String = "",
    var angulationData: String = "",
    var recommendedAction: String = "",
    var observation: String = ""
): Serializable {
    fun getUserDate(): String {
        val simpleDateFormat: DateFormat = SimpleDateFormat.getDateInstance()
        return simpleDateFormat.format(date)
    }

    fun getUserTime(): String {
        val simpleDateFormat: DateFormat = SimpleDateFormat.getTimeInstance()
        return simpleDateFormat.format(date)
    }
}