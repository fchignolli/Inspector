package com.example.inspector.Model

import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Lucas Alves dos Santos on 29/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
data class Tool(
    var id: String = "",
    val date: Date? = null,
    var placeName: String = "",
    var inspectedName: String = "",
    var barometerResolution: String = "",
    var thermometerResolution: String = "",
    var rulerResolution: String = "",
    var hasSimulatorDosimetry: Boolean = false,
    var hasLaserAligner: Boolean = false,
    var hasStabilityChecker: Boolean = false,
    var observation: String = "",
    var images: ArrayList<Image> = ArrayList()
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

