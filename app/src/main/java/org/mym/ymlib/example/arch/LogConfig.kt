package org.mym.ymlib.example.arch

import android.app.Application
import android.content.Context
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationAttachBaseContext
import org.mym.ymlib.annotation.OnApplicationCreate

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
}