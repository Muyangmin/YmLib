package org.mym.ymlib.example.arch

import android.app.Application
import android.content.Context
import org.mym.ymlib.annotation.*

@ApplicationLifecycleAware
class LogConfig {

    @OnApplicationAttachBaseContext
    @Ordered(1)
    fun onAttachBaseContext(context: Context) {
        println("It works")
    }

    @OnApplicationCreate
    fun onCreate(application: Application) {
        println("I'm trying to inject log config into application.")
    }

    @OnApplicationExit
    fun onExit(code: Int) {
        println("On Exit")
    }
}