package com.example.inspector.Model

import android.net.Uri
import java.util.*

/**
 * Created by Lucas Alves dos Santos on 09/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class Image (
    var id: String = "",
    var fileName: String = "",
    val createDate: Date? = null,
    var description: String? = "",
    var uriString: String = ""
)