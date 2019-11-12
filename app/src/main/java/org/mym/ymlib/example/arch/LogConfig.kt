package org.mym.ymlib.example.arch

import org.mym.ymlib.annotation.ApplicationLifecycleAware

@ApplicationLifecycleAware
class LogConfig {

    fun onCreate() {
        println("I'm trying to inject log config into application.")
    }
}