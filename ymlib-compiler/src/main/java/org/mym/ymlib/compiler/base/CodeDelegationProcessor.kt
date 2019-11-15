package org.mym.ymlib.compiler.base

import com.squareup.javapoet.*
import org.mym.ymlib.annotation.Ordered
import org.mym.ymlib.compiler.data.CallMapping
import org.mym.ymlib.compiler.data.DelegateMapping
import org.mym.ymlib.compiler.data.MappingMode
import org.mym.ymlib.compiler.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*

/**
 * 用于生成一系列与被代理对象一样签名的方法，达到使用注解解耦的目的。
 *
 * @property[packageName] 待生成文件的包名。
 * @property[className] 待生成文件的类名。
 * @property[typeAnnotation] 代理类的注解。只有注解的类，其内部的注解方法才会被识别。
 */
open class CodeDelegationProcessor(
    private val packageName: String,
    private val className: String,
    private val typeAnnotation: Class<out Annotation>
) : AbstractProcessor() {

    private var roundCount: Int = 0

    /**
     * 记录 Annotation 到被代理对象的映射。
     */
    private val delegateMappings = mutableListOf<DelegateMapping>()

    /**
     * 记录每种映射关系对应的已发现的代理调用列表。
     */
    private val callMappings =
        mutableMapOf<DelegateMapping, MutableList<CallMapping>>()

    /**
     * 注册代理映射。为方便使用，可以直接使用字符串形式的类名和方法名，方法实现内部将自动转换为合适的数据结构。
     *
     * 注意，映射重载方法可能出现未定义的行为，强烈不推荐这样做。
     */
    protected fun addMapping(
        annotationClz: Class<out Annotation>,
        classQualifier: String,
        methodName: String
    ) {
        val elements = processingEnv.elementUtils
        val typeElement = elements.getTypeElement(classQualifier)

        //尝试查找匹配的方法，如果没有则抛出异常
        val targetElement = processingEnv.findTargetExecutableElement(typeElement, methodName)
        val mappingObj = DelegateMapping(annotationClz, typeElement, targetElement)
        delegateMappings.add(mappingObj)
        callMappings[mappingObj] = mutableListOf()
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.RELEASE_8

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        processingEnv.note("-----> ${this::class.java.simpleName}#process(Round $roundCount) Start")
        if (annotations.isNotEmpty() && delegateMappings.isNotEmpty()) {
            if (checkAndRegister(roundEnv)) {
                generateFinalFile()
            }
        }
        processingEnv.note("-----> ${this::class.java.simpleName}#process(Round $roundCount) End")
        roundCount++
        return true
    }

    /**
     * 为每一个具体的调用方法判断其应该用什么方式调用。如果不满足已知的模式则将返回 null。
     */
    private fun detectMappingMode(
        callMethod: ExecutableElement,
        targetType: TypeElement,
        targetMethod: ExecutableElement
    ): MappingMode? {
        return when {
            callMethod.parameters.isEmpty() -> MappingMode.NO_ARGS
            callMethod.parameters.size == targetMethod.parameters.size && isAllSameType(
                callMethod.parameters, targetMethod.parameters
            ) -> MappingMode.ACCURATE_PARAM
            callMethod.parameters.size == 1 && processingEnv.typeUtils.isAssignable(
                targetType.asType(), callMethod.parameters[0].asType()
            ) -> MappingMode.REF
            callMethod.parameters.size == targetMethod.parameters.size + 1 && isAllSameType(
                callMethod.parameters.subList(1, callMethod.parameters.size),
                targetMethod.parameters
            ) -> MappingMode.REF_AND_PARAM
            else -> null
        }
    }

    private fun isAllSameType(
        actual: List<VariableElement>,
        expected: List<VariableElement>
    ): Boolean {
        var isSameType = true
        for (index in actual.indices) {
            val actualType = actual[index].asType()
            val expectedType = expected[index].asType()
            if (!processingEnv.typeUtils.isSameType(actualType, expectedType)) {
                isSameType = false
                break
            }
        }
        return isSameType
    }

    /**
     * 根据映射关系，发现和填充调用列表。
     */
    private fun checkAndRegister(roundEnv: RoundEnvironment): Boolean {
        for (element in roundEnv.getElementsAnnotatedWith(typeAnnotation)) {
            //检查被注解的类是否满足条件
            if (!checkTypeElement(element)) {
                return false
            }
            delegateMappings.forEach { mapping ->
                val annotatedElements = element.enclosedElements.filter {
                    it.getAnnotation(mapping.annotation) != null
                }
                annotatedElements.forEach {
                    if (!checkExecutableElement(it, mapping.annotation)) {
                        return false
                    }
                    val mappingMode = requireNotNull(
                        detectMappingMode(
                            (it as ExecutableElement),
                            mapping.delegateClass, mapping.delegateMethod
                        )
                    ) {
                        "Cannot decide how to call the method $it"
                    }
                    callMappings[mapping]?.add(
                        CallMapping(
                            element as TypeElement,
                            it,
                            mappingMode
                        )
                    )
                }
            }
        }
        return true
    }

    /**
     * 检查类元素是否满足要求。
     *
     * @return 如果满足所有条件则返回 `true`，否则返回 `false`。
     */
    private fun checkTypeElement(element: Element): Boolean {
        val error: String? = when {
            //检查 element 类型：即必须注解在类上
            element.kind != ElementKind.CLASS || element !is TypeElement ->
                "Annotation $typeAnnotation can only be applied to a class."
            //被注解的类必须为 Public
            !element.modifiers.contains(Modifier.PUBLIC) ->
                "Class annotated with $typeAnnotation must be public."
            //被注解的类不能为 abstract
            element.modifiers.contains(Modifier.ABSTRACT) ->
                "Class annotated with $typeAnnotation cannot be abstract."
            //被注解的类必须提供无参构造函数
            run {
                val constructors =
                    element.enclosedElements.filter { it.kind == ElementKind.CONSTRUCTOR }
                constructors.none {
                    (it as ExecutableElement).parameters.size == 0 && it.modifiers.contains(Modifier.PUBLIC)
                }
            } -> "Class annotated with $typeAnnotation must provide an public empty default constructor."
            else -> null
        }
        return if (!error.isNullOrEmpty()) {
            processingEnv.error(error, element)
            false
        } else {
            true
        }
    }

    /**
     * 检查方法元素是否满足要求。注意，这里并不检查方法的参数是否匹配，相反，匹配任务由 [detectMappingMode] 完成。
     *
     * @return 如果满足所有条件则返回 `true`，否则返回 `false`。
     */
    private fun checkExecutableElement(
        element: Element,
        annotationClz: Class<out Annotation>
    ): Boolean {
        val error: String? = when {
            //注解只能应用于方法上
            element.kind != ElementKind.METHOD || element !is ExecutableElement ->
                "Annotation $annotationClz can only be applied to methods."
            //被注解的方法必须为 Public
            !element.modifiers.contains(Modifier.PUBLIC) ->
                "Method annotated with $annotationClz must be public."
            //被注解的方法不能为 abstract
            element.modifiers.contains(Modifier.ABSTRACT) ->
                "Method annotated with $annotationClz cannot be abstract."
            else -> null
        }
        return if (!error.isNullOrEmpty()) {
            processingEnv.error(error, element)
            false
        } else {
            true
        }
    }


    /**
     * 根据已有的调用映射列表生成最终的 Java 文件。
     */
    private fun generateFinalFile() {
        val typeBuilder = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addFields(buildNecessaryFields())

        delegateMappings.forEach {
            typeBuilder.addMethod(buildDelegateMethod(it))
        }

        val file = JavaFile.builder(packageName, typeBuilder.build())
            .build()
        file.writeTo(processingEnv.filer)
    }

    private fun buildNecessaryFields(): List<FieldSpec> {
        return callMappings.flatMap { it.value }
            .distinctBy { it.callClass.qualifiedName }
            .map {
                val type = ClassName.get(it.callClass.asType())
                FieldSpec.builder(
                    type, decideFieldName(it.callClass)
                ).addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new \$T()", type)
                    .build()
            }
    }

    /**
     * 生成每个映射关系对应的代理方法。
     */
    private fun buildDelegateMethod(delegateMapping: DelegateMapping): MethodSpec {
        val delegateMethod = delegateMapping.delegateMethod
        val builder = MethodSpec.methodBuilder(delegateMethod.simpleName.toString())
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(TypeName.VOID)

        //按需注入 ref 参数
        val calls = callMappings[delegateMapping]
        if (calls != null && calls.any {
                it.callMode == MappingMode.REF || it.callMode == MappingMode.REF_AND_PARAM
            }) {
            builder.addParameter(
                ClassName.get(delegateMapping.delegateClass.asType()),
                decideRefArgName(delegateMapping.delegateClass)
            )
        }
        //无论是否有方法需要，都将原有参数注入
        delegateMethod.parameters.forEach { param ->
            builder.addParameter(
                ClassName.get(param.asType()), decideMethodArgName(param)
            )
        }
        //注入全部方法调用，如果有指定了 order 则按照 order 顺序，否则默认最小优先级
        calls?.sortedBy {
            it.callMethod.getAnnotation(Ordered::class.java)?.order ?: Int.MAX_VALUE
        }?.forEach { callMapping ->
            //不能使用 AddStatement 的方式来调用，因为 Statement 是不能嵌套的。
            builder.addCode(buildDelegateMethodCall(delegateMapping, callMapping))
        }
        return builder.build()
    }
//
//    private fun decideFieldName(fieldType: TypeElement): String {
//        return fieldType.simpleName.toString().unCapitalize()
//    }
//
//    private fun decideRefArgName(ref: TypeElement): String {
//        return ref.simpleName.toString().lastWord()
//    }
//
//    private fun decideMethodArgName(arg: VariableElement): String {
//        return arg.simpleName.toString()
////        return ClassName.get(arg.asType()).toString().lastWord()
//    }

    /**
     * 生成单条方法调用。例如 `Foo.bar(arg1, arg2, arg3)`
     */
    private fun buildDelegateMethodCall(
        delegateMapping: DelegateMapping,
        callMapping: CallMapping
    ): CodeBlock {
        val builder = CodeBlock.builder()

        //拼接参数字符串。
        //注意，因为方法在命名参数的时候只能参考原映射方法的定义，所以这里也需要使用映射方法的参数名（而不管被调用的方法是如何命名形参）。
        val paramLiteral = when (callMapping.callMode) {
            MappingMode.NO_ARGS -> ""
            MappingMode.ACCURATE_PARAM -> delegateMapping.delegateMethod.parameters.joinToString {
                decideMethodArgName(it)
            }
            MappingMode.REF -> decideRefArgName(delegateMapping.delegateClass)
            MappingMode.REF_AND_PARAM -> delegateMapping.delegateMethod.parameters.joinToString(
                prefix = decideRefArgName(delegateMapping.delegateClass) + ", "
            ) {
                decideMethodArgName(it)
            }
        }
        builder.addStatement(
            "\$L.\$N(\$L)",
            decideFieldName(callMapping.callClass),
            callMapping.callMethod.simpleName,
            paramLiteral
        )

        return builder.build()
    }
}