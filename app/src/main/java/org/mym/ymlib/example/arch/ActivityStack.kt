package org.mym.ymlib.example.arch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.mym.ymlib.YmApplication
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationCreate
import org.mym.ymlib.annotation.OnApplicationExit
import kotlin.reflect.KClass

@ApplicationLifecycleAware
class ActivityStack {

    val activityList = mutableListOf<Activity>()

    //Test ref
    @OnApplicationCreate
    fun onCreate(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                //Empty
            }

            override fun onActivityResumed(activity: Activity?) {
                //Empty
            }

            override fun onActivityStarted(activity: Activity) {
                //Empty
            }

            override fun onActivityDestroyed(activity: Activity) {
                //Empty
                activityList.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                //Empty
            }

            override fun onActivityStopped(activity: Activity?) {
                //Empty
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                //Empty
                activityList.add(activity)
            }
        })
    }

    fun <T : Activity> finishExcept(clz: KClass<T>) {
        for (act in activityList) {
            if (act::class != clz) {
                act.finish()
            }
        }
    }

    //Test different method param name
    @OnApplicationExit
    fun exit(whatever: Int) {
        activityList.forEach {
            try {
                if (!it.isFinishing) {
                    it.finish()
                }
            } catch (ignored: Throwable) {
            }
        }
        activityList.clear()
    }

}

fun <T: Activity> YmApplication.finishExcept(clz: KClass<T>) = applicationDelegate.activityStack.finishExcept(clz)