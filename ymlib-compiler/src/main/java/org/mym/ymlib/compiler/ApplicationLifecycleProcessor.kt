package org.mym.ymlib.compiler

import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationAttachBaseContext
import org.mym.ymlib.annotation.OnApplicationCreate
import org.mym.ymlib.annotation.OnApplicationExit
import org.mym.ymlib.compiler.base.CodeDelegationProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.mym.ymlib.annotation.ApplicationLifecycleAware")
class ApplicationLifecycleProcessor : CodeDelegationProcessor(
    "org.mym.ymlib",
    "ApplicationDelegate",
    ApplicationLifecycleAware::class.java
) {

    companion object {
        private const val QUALIFIER_APPLICATION = "android.app.Application"
        private const val QUALIFIER_MANUAL_EXIT_APP = "org.mym.ymlib.ext.ManualExitApplication"
        private const val QUALIFIER_YM_APPLICATION = "org.mym.ymlib.arch.YmApplication"
        private const val METHOD_ON_ATTACH_BASE = "attachBaseContext"
        private const val METHOD_ON_CREATE = "onCreate"
        private const val METHOD_ON_EXIT = "onExit"
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        addMapping(
            OnApplicationAttachBaseContext::class.java,
            QUALIFIER_APPLICATION,
            METHOD_ON_ATTACH_BASE
        )
        addMapping(OnApplicationCreate::class.java, QUALIFIER_APPLICATION, METHOD_ON_CREATE)
        addMapping(OnApplicationExit::class.java, QUALIFIER_MANUAL_EXIT_APP, METHOD_ON_EXIT)
    }
}