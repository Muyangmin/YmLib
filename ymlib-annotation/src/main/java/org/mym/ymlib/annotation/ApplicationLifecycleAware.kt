package org.mym.ymlib.annotation

import org.mym.ymlib.ext.ManualExitApplication

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class ApplicationLifecycleAware

/**
 * 用于注解该方法需要在 onAttachBaseContext 时被调用。
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class OnApplicationAttachBaseContext

/**
 * 用于注解该方法需要在 onCreate 时被调用。
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class OnApplicationCreate

/**
 * 用于注解该方法需要在 onExit(code) 时被调用。
 *
 * 注意：这个方法并不是 Application 自带的，而是额外添加的，方法签名等于 [ManualExitApplication.onExit]。
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class OnApplicationExit