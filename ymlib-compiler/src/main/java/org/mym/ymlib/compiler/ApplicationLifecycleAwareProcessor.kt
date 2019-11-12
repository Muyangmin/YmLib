package org.mym.ymlib.compiler

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import org.mym.ymlib.annotation.ApplicationLifecycleAware
import org.mym.ymlib.compiler.util.error
import org.mym.ymlib.compiler.util.note
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("org.mym.ymlib.annotation.ApplicationLifecycleAware")
class ApplicationLifecycleAwareProcessor : AbstractProcessor() {

    private var roundCount: Int = 0

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        processingEnv.note("-----> ApplicationLifecycleAwareProcessor#process(Round $roundCount) Start")
        if (!annotations.isNullOrEmpty()) {
            processingEnv.note(annotations.joinToString { it.qualifiedName })
            for (element in roundEnv.getElementsAnnotatedWith(ApplicationLifecycleAware::class.java)) {
                if (!element.isValidElement()) {
                    return false
                }
                //TODO add element to collection and then generate code
            }
        }
//        val method = MethodSpec.methodBuilder("onCreate")
//            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
//            .addStatement("\$T.out.println(\$S)", System::class.java, "Hello, JavaPoet!")
//            .returns(TypeName.VOID)
//            .build()
//
//        val injector = TypeSpec.classBuilder("ApplicationInjector")
//            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//            .addMethod(method)
//            .build()
//
//        val file = JavaFile.builder("org.mym.ymlib", injector)
//            .build()
//
//        file.writeTo(processingEnv.filer)
        processingEnv.note("-----> ApplicationLifecycleAwareProcessor#process(Round $roundCount) End")
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
        //TODO consider use an interface to normalize method names
        return true
    }
}