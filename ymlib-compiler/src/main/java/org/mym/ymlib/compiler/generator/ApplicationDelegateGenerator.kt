package org.mym.ymlib.compiler.generator

import com.squareup.javapoet.*
import org.mym.ymlib.compiler.ApplicationLifecycleProcessor
import org.mym.ymlib.compiler.util.QUALIFIER_APPLICATION
import org.mym.ymlib.compiler.util.QUALIFIER_CONTEXT
import org.mym.ymlib.compiler.util.lastWord
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ApplicationDelegateGenerator(private val processingEnv: ProcessingEnvironment) {

    companion object {
        private const val GENERATE_CLASS_NAME = "ApplicationDelegate"

        const val DELEGATE_METHOD_CREATE = "onCreate"
        const val DELEGATE_METHOD_ATTACH = "onAttachBaseContext"
    }

    private val compilerPackageName = ApplicationLifecycleProcessor::class.java.`package`.name

    private val attachBaseContextCalls = mutableListOf<Pair<TypeElement, ExecutableElement>>()
    private val onCreateCalls = mutableListOf<Pair<TypeElement, ExecutableElement>>()

    fun registerCall(
        delegateMethod: String,
        classElement: TypeElement,
        methodElement: ExecutableElement
    ) {
        when (delegateMethod) {
            DELEGATE_METHOD_ATTACH -> attachBaseContextCalls.add(classElement to methodElement)
            DELEGATE_METHOD_CREATE -> onCreateCalls.add(classElement to methodElement)
            else -> throw UnsupportedOperationException("Unsupported method $delegateMethod")
        }
    }

    fun generate() {
        val typeSpec = TypeSpec.classBuilder(GENERATE_CLASS_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(
                generateMethodSpec(
                    DELEGATE_METHOD_ATTACH,
                    attachBaseContextCalls,
                    listOf(QUALIFIER_CONTEXT)
                )
            )
            .addMethod(
                generateMethodSpec(
                    DELEGATE_METHOD_CREATE,
                    onCreateCalls,
                    listOf(QUALIFIER_APPLICATION)
                )
            )
            .build()
        val file = JavaFile.builder(compilerPackageName, typeSpec)
            .build()

        file.writeTo(processingEnv.filer)
    }

    private fun generateMethodSpec(
        methodName: String,
        statementCalls: Collection<Pair<TypeElement, ExecutableElement>>,
        paramQualifiers: List<String>
    ): MethodSpec {
        val builder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .returns(TypeName.VOID)
        //如果方法允许有参数，则将参数注入
        paramQualifiers.forEach { qualifier ->
            builder.addParameter(
                ParameterSpec.builder(
                    ClassName.get(processingEnv.elementUtils.getTypeElement(qualifier)),
                    qualifier.lastWord()
                ).build()
            )
        }
        //对于每个方法调用，如果其有参数则注入参数，否则做无参调用
        statementCalls.forEach { (type, method) ->
            val className = ClassName.get(type)
            val callName = method.simpleName
            val callParam = if (method.parameters.size > 0) {
                paramQualifiers.joinToString { it.lastWord() }
            } else {
                ""
            }
            builder.addStatement("new \$T().\$L(\$L)", className, callName, callParam)
        }
        return builder.build()
    }
}