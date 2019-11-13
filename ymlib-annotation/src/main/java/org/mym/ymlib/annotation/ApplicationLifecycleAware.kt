package org.mym.ymlib.annotation

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