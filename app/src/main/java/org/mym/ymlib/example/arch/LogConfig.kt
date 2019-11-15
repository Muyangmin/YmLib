package org.mym.ymlib.example.arch

import android.app.Application
import android.content.Context
import org.mym.ymlib.annotation.*

@ApplicationLifecycleAware
class LogConfig {

    //Test ordered ref-param
    @OnApplicationAttachBaseContext
    @Ordered(1)
    fun onAttachBaseContext(context: Context, code: Any) {
        println("It works")
    }

    //Test NO-args
    @OnApplicationCreate
    fun onCreate() {
        println("I'm trying to inject log config into application.")
    }

    //TEST accurate
    @OnApplicationExit
    fun onExit(code: Int) {
        println("On Exit")
    }
}