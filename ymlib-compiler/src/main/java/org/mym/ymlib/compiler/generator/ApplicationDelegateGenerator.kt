package org.mym.ymlib.compiler.generator

import com.squareup.javapoet.*
import org.mym.ymlib.compiler.ApplicationLifecycleProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ApplicationDelegateGenerator(private val processingEnv: ProcessingEnvironment) {

    companion object {
        private const val DELEGATE_METHOD_CREATE = "onCreate"
        private const val DELEGATE_METHOD_ATTACH = "onAttachBaseContext"
    }

    private val modelMap = mutableMapOf<String, List<MethodModel>>()
    private val compilerPackageName = ApplicationLifecycleProcessor::class.java.`package`.name
    private val supportedMethods = listOf(DELEGATE_METHOD_ATTACH, DELEGATE_METHOD_CREATE)

    private val applicationTypeElement =
        processingEnv.elementUtils.getTypeElement("android.app.Application")

    private fun registerMethod(
        mapMethod: String,
        classElement: TypeElement,
        methodElement: Collection<Element>
    ) {
        if (methodElement.isEmpty()) {
            return
        }
        modelMap[mapMethod] = modelMap[mapMethod].orEmpty().plus(
            methodElement.map {
                val className = classElement.qualifiedName.toString()
                val methodName = it.simpleName.toString()
                MethodModel(className, methodName)
            }
        )
    }

    fun registerOnAttachBaseContext(
        classElement: TypeElement,
        methodElement: Collection<Element>
    ) {
        registerMethod(DELEGATE_METHOD_ATTACH, classElement, methodElement)
    }

    fun registerOnCreate(classElement: TypeElement, methodElement: Collection<Element>) {
        registerMethod(DELEGATE_METHOD_CREATE, classElement, methodElement)
    }

    fun generate() {
        val methods = supportedMethods.mapNotNull {
            MethodSpec.methodBuilder(it)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addParameter(
                    ParameterSpec.builder(
                        ClassName.get(applicationTypeElement),
                        "app"
                    ).build()
                )
                .apply {
                    val invokeMethods = modelMap[it]
                    invokeMethods?.forEach { model ->
                        addStatement("new ${model.className}().${model.methodName}(app)")
                    }
                }
                .returns(TypeName.VOID)
                .build()
        }
        val typeSpec = TypeSpec.classBuilder("ApplicationDelegate")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .apply {
                methods.forEach {
                    addMethod(it)
                }
            }
            .build()
        val file = JavaFile.builder(compilerPackageName, typeSpec)
            .build()

        file.writeTo(processingEnv.filer)
    }

    private data class MethodModel(
        val className: String,
        val methodName: String
    )
}