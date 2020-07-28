package com.example.inspector.Model

import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Lucas Alves dos Santos on 28/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
data class Room(
    var id: String = "",
    val date: Date? = null,
    var placeName: String = "",
    var inspectedName: String = "",
    var hasSecurityLight: Boolean = false,
    var hasElectronicDevice: Boolean = false,
    var hasCommunicateDevice: Boolean = false,
    var hasOpenDevice: Boolean = false,
    var hasOpenExternalDevice: Boolean = false,
    var hasLightingDevice: Boolean = false,
    var hasButtons: Boolean = false,
    var hasSymbol: Boolean = false,
    var hasInformation: Boolean = false,
    var hasActionPlan: Boolean = false,
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