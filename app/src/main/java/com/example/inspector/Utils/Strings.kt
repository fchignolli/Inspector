package com.example.inspector.Utils

import androidx.annotation.StringRes

/**
 * Created by Lucas Alves dos Santos on 19/05/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}