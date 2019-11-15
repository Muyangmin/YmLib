package org.mym.ymlib.compiler

import com.squareup.javapoet.*
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationAttachBaseContext
import org.mym.ymlib.annotation.OnApplicationCreate
import org.mym.ymlib.annotation.OnApplicationExit
import org.mym.ymlib.compiler.base.CodeDelegationProcessor
import org.mym.ymlib.compiler.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.mym.ymlib.annotation.ApplicationLifecycleAware")
class ApplicationLifecycleProcessor : CodeDelegationProcessor(
    PACKAGE_NAME,
    DELEGATE_CLASS_NAME,
    ApplicationLifecycleAware::class.java
) {

    companion object {
        private const val PACKAGE_NAME = "org.mym.ymlib"
        private const val DELEGATE_CLASS_NAME = "ApplicationDelegate"
        private const val GENERATE_APP_CLASS_NAME = "YmApplication"

        private const val QUALIFIER_APPLICATION = "android.app.Application"
        private const val QUALIFIER_MANUAL_EXIT_APP = "org.mym.ymlib.ext.ManualExitApplication"
        private const val QUALIFIER_GEN_DELEGATE = "$PACKAGE_NAME.$DELEGATE_CLASS_NAME"

        private const val METHOD_ON_ATTACH_BASE = "attachBaseContext"
        private const val METHOD_ON_CREATE = "onCreate"
        private const val METHOD_ON_EXIT = "onExit"
    }

    private var appFileCreated: Boolean = false

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

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val handled = super.process(annotations, roundEnv)
        if (handled && !appFileCreated) {
            appFileCreated = generateApplicationFile()
        }
        return handled
    }

    private fun generateApplicationFile(): Boolean {
        //检查代理类是否已经生成，仅在生成后才能生成被代理的 Application。
        processingEnv.elementUtils.getTypeElement("$PACKAGE_NAME.$DELEGATE_CLASS_NAME") ?: return false

        val typeSpec = TypeSpec.classBuilder(GENERATE_APP_CLASS_NAME)
            .addModifiers(Modifier.PUBLIC)
            .superclass(processingEnv.classNameFromQualifier(QUALIFIER_APPLICATION))
            .addSuperinterface(processingEnv.classNameFromQualifier(QUALIFIER_MANUAL_EXIT_APP))
            .addField(buildFieldSpec(processingEnv.typeElementFromQualifier(QUALIFIER_GEN_DELEGATE)))
            .addMethods(buildMethods())

        val javaFile = JavaFile.builder(PACKAGE_NAME, typeSpec.build())
            .build()
//        processingEnv.note(javaFile.toString())
        javaFile.writeTo(processingEnv.filer)
        return true
    }

    private fun buildMethods(): List<MethodSpec> {
        val methods = arrayOf(
            QUALIFIER_APPLICATION to METHOD_ON_ATTACH_BASE,
            QUALIFIER_APPLICATION to METHOD_ON_CREATE,
            QUALIFIER_MANUAL_EXIT_APP to METHOD_ON_EXIT
        )

        val delegate = processingEnv.typeElementFromQualifier(QUALIFIER_GEN_DELEGATE)
        return methods.mapNotNull { (className, methodName) ->
            val parentType = processingEnv.typeElementFromQualifier(className)
            val parentMethod = processingEnv.findTargetExecutableElement(parentType, methodName)
            val params = parentMethod.parameters.map { param ->
                ParameterSpec.builder(ClassName.get(param.asType()), decideMethodArgName(param))
                    .build()
            }

            val callMethod = processingEnv.findTargetExecutableElement(delegate, methodName)
            val originParams = params.joinToString { it.name }
            val invokeParam = when (callMethod.parameters.size) {
                //参数完全相同
                parentMethod.parameters.size -> originParams
                //参数多出一个 ref
                parentMethod.parameters.size + 1 -> arrayOf(
                    "this",
                    originParams
                ).filter { it.isNotEmpty() }.joinToString()
                else -> {
                    processingEnv.error("Unable to decide how to call delegate method $methodName")
                    throw RuntimeException("See error above.")
                }
            }
            MethodSpec.methodBuilder(methodName)
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .addParameters(params)
                .addStatement("\$L.\$L(\$L)", decideFieldName(delegate), methodName, invokeParam)
                .build()
        }
    }
}