package org.mym.ymlib.compiler

import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.annotation.OnApplicationAttachBaseContext
import org.mym.ymlib.annotation.OnApplicationCreate
import org.mym.ymlib.compiler.generator.ApplicationDelegateGenerator
import org.mym.ymlib.compiler.util.QUALIFIER_APPLICATION
import org.mym.ymlib.compiler.util.QUALIFIER_CONTEXT
import org.mym.ymlib.compiler.util.error
import org.mym.ymlib.compiler.util.note
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.mym.ymlib.annotation.ApplicationLifecycleAware")
class ApplicationLifecycleProcessor : AbstractProcessor() {

    private var roundCount: Int = 0

    private lateinit var generator: ApplicationDelegateGenerator

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        generator = ApplicationDelegateGenerator(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        processingEnv.note("-----> ApplicationLifecycleProcessor#process(Round $roundCount) Start")
        if (!annotations.isNullOrEmpty()) {
            for (element in roundEnv.getElementsAnnotatedWith(ApplicationLifecycleAware::class.java)) {
                if (!element.isValidElement()) {
                    return false
                }

                if (!checkAndRegister(
                        ApplicationDelegateGenerator.DELEGATE_METHOD_ATTACH,
                        element as TypeElement,
                        OnApplicationAttachBaseContext::class.java,
                        listOf(QUALIFIER_CONTEXT)
                    )
                ) {
                    return false
                }

                if (!checkAndRegister(
                        ApplicationDelegateGenerator.DELEGATE_METHOD_CREATE,
                        element,
                        OnApplicationCreate::class.java,
                        listOf(QUALIFIER_APPLICATION)
                    )
                ) {
                    return false
                }
            }
            generator.generate()
        }
        processingEnv.note("-----> ApplicationLifecycleProcessor#process(Round $roundCount) End")
        roundCount++
        return true
    }

    private fun Element.isValidElement(): Boolean {
        if (kind != ElementKind.CLASS) {
            processingEnv.error("${ApplicationLifecycleAware::class.java.simpleName} 只能用于 Class.")
            return false
        }
        val typeElement = this as TypeElement
        if (!typeElement.modifiers.contains(Modifier.PUBLIC)
            || typeElement.modifiers.contains(Modifier.ABSTRACT)
        ) {
            processingEnv.error("被 ${ApplicationLifecycleAware::class.java.simpleName} 注解的类必须为 public 且不能为 abstract")
            return false
        }
        return true
    }

    private fun checkAndRegister(
        delegateMethod: String,
        typeElement: TypeElement,
        annotationClz: Class<out Annotation>,
        allowedParamQualifiers: List<String>
    ): Boolean {
        typeElement.enclosedElements.filter {
            it.getAnnotation(annotationClz) != null
        }.forEach {
            if (!checkMethod(it, annotationClz, allowedParamQualifiers)) {
                return false
            }
            generator.registerCall(delegateMethod, typeElement, it as ExecutableElement)
        }
        return true
    }

    private fun checkMethod(
        element: Element,
        annotationClz: Class<out Annotation>,
        allowedParamQualifiers: List<String>
    ): Boolean {
        val annotation = annotationClz.simpleName
        if (element.kind != ElementKind.METHOD) {
            processingEnv.error("$annotation can only apply to methods.")
            return false
        }
        if (element.modifiers.contains(Modifier.ABSTRACT)) {
            processingEnv.error("$annotation cannot apply to an abstract method")
            return false
        }
        val executableElement = element as ExecutableElement
        if (executableElement.parameters.size != 0 && executableElement.parameters.size != allowedParamQualifiers.size) {
            processingEnv.error(
                "Method ${executableElement.asType()} with $annotation should either has no parameter " +
                        "or accurate ${allowedParamQualifiers.size} parameters, but found ${executableElement.parameters.size}."
            )
            return false
        }

        executableElement.parameters.forEachIndexed { index, variableElement ->
            val actualType = variableElement.asType()
            val expectedType =
                processingEnv.elementUtils.getTypeElement(allowedParamQualifiers[index]).asType()
            if (!processingEnv.typeUtils.isSameType(actualType, expectedType)) {
                processingEnv.error("Method with $annotation parameter mismatch, expected $expectedType on index $index, found $actualType")
                return false
            }
        }
        return true
    }
}