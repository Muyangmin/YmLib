package org.mym.ymlib.example

import android.app.Application
import android.content.Context

class SampleApplication : Application() {
    companion object {
        lateinit var instance: SampleApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }
}