package org.mym.ymlib.example.arch

import android.app.Application
import android.content.Context
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationAttachBaseContext
import org.mym.ymlib.annotation.OnApplicationCreate
import org.mym.ymlib.annotation.OnApplicationExit

@ApplicationLifecycleAware
class LogConfig {

    @OnApplicationAttachBaseContext
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