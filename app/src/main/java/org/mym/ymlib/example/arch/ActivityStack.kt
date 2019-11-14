package org.mym.ymlib.example.arch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationCreate

@ApplicationLifecycleAware
class ActivityStack {

    @OnApplicationCreate
    fun onCreate(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                // Intend empty
            }

            override fun onActivityResumed(activity: Activity?) {
                // Intend empty
            }

            override fun onActivityStarted(activity: Activity?) {
                // Intend empty
            }

            override fun onActivityDestroyed(activity: Activity?) {
                // Intend empty
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                // Intend empty
            }

            override fun onActivityStopped(activity: Activity?) {
                // Intend empty
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                // Intend empty
            }
        })
    }

}