package com.example.inspector.Utils

import android.app.Application

/**
 * Created by Lucas Alves dos Santos on 19/05/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class App: Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}